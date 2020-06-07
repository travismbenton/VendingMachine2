/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine2.service;

import com.sg.vendingmachine2.dao.VendingMachineAuditDao;
import com.sg.vendingmachine2.dao.VendingMachineDao;
import com.sg.vendingmachine2.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine2.dto.Inventory;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author travi
 */
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer {
    
    VendingMachineDao dao;
    VendingMachineAuditDao auditDao;

public VendingMachineServiceLayerImpl(VendingMachineDao dao, 
        VendingMachineAuditDao auditDao){
    this.dao = dao;
    this.auditDao = auditDao;
}


    @Override
    public void createInventory(Inventory inventory) throws              
            VendingMachinePersistenceException, VendingMachineDuplicateItemException,
            VendingMachineDataValidationException {
        
        if (dao.getInventory(inventory.getItemName()) != null) {
            throw new VendingMachineDuplicateItemException (
            "Error: Could not create new Item. Item ["
                    +inventory.getItemName()+
                    "] already exists!");
        }
        validateItemData(inventory);
        dao.addInventory(inventory.getItemName(), inventory);
        auditDao.writeAuditEntry(
                "New inventory "+inventory.getItemName()+" CREATED.");
    } 
    
    @Override
    public void editInventory(Inventory inventory) throws VendingMachinePersistenceException, 
            VendingMachineDataValidationException {
        validateItemData(inventory);
        dao.addInventory(inventory.getItemName(), inventory);
        auditDao.writeAuditEntry(
                "Inventory "+ inventory.getItemName() + " EDITED.");
    }
    

    @Override
    public List<Inventory> getAllInventory() throws 
            VendingMachinePersistenceException {
        return dao.getAllInventory();
    }

    @Override
    public Inventory getInventory(String itemName) throws 
            VendingMachinePersistenceException {
        return dao.getInventory(itemName);
    }

    @Override
    public Inventory removeInventory(String itemName) throws 
            VendingMachinePersistenceException {
        Inventory removedInventory = dao.removeInventory(itemName);
        auditDao.writeAuditEntry(
                "New inventory "+itemName+" REMOVED.");
        return dao.removeInventory(itemName);
    }
    
      
    public void validateItemData(Inventory inventory) throws 
            VendingMachineDataValidationException {
        
       if (inventory.getItemCost() == null
            || inventory.getItemCost().trim().length() == 0
            || inventory.getNumberOfItemsInInventory() == null
            || inventory.getNumberOfItemsInInventory().trim().length() == 0) {

        throw new VendingMachineDataValidationException (
                "ERROR: All fields [Item Cost, Number Of Items] are required.");         
        }       
    } 
    
    
    public Inventory validateNewItemInInventory(String itemName) throws               
            VendingMachinePersistenceException, VendingMachineNoItemInventoryException {
        return dao.getInventory(itemName);
    }     
  
    
@Override
    public void validateItemInInventory(int itemsRemaining, String nameOfItem) throws               
            VendingMachinePersistenceException, VendingMachineNoItemInventoryException {
        
        if (nameOfItem.equalsIgnoreCase("Chips") && itemsRemaining == 0
            || nameOfItem.equalsIgnoreCase("Candy Bar") && itemsRemaining == 0    
            || nameOfItem.equalsIgnoreCase("Soda") && itemsRemaining == 0 
            || nameOfItem.equalsIgnoreCase("Sandwich") && itemsRemaining == 0
            || nameOfItem.equalsIgnoreCase("Cookies") && itemsRemaining == 0){
            
            throw new VendingMachineNoItemInventoryException(""
                +"Please make another selection.");            
        }  else {
                 System.out.println(" Available for purchase!");
            } 
    }    
    
       
@Override
    public double validatePrice(double itemPrice) throws 
        VendingMachinePersistenceException, VendingMachineInsufficientFundsException {
           
           double userMoney;           
           
           Scanner sc = new Scanner(System.in);        
           System.out.println("Please enter dollars or coins for your selection. ");
           userMoney = sc.nextDouble();      
           
       if (userMoney < itemPrice){
            throw new VendingMachineInsufficientFundsException(""
                +"InsufficientFundsException. "+userMoney );
       } 
        return userMoney;
    } 
    
        
    public int auditLog(int itemsRemaining, String nameOfItem, String priceOfItem) 
            throws VendingMachinePersistenceException{
        
        switch (nameOfItem){
                case "Chips":
                    itemsRemaining = itemsRemaining - 1;
                    break;
                case "Candy Bar":
                    itemsRemaining = itemsRemaining - 1;
                    break;
                case "Soda":
                    itemsRemaining = itemsRemaining - 1;
                    break;
                case "Sandwich":
                    itemsRemaining = itemsRemaining - 1;
                    break;
                case "Cookies":
                    itemsRemaining = itemsRemaining - 1;
                    break;
            }   
               
            auditDao.writeAuditEntry(
                nameOfItem+": Vended. Items Remaining: "+itemsRemaining);
            
            String numberOfItemsInInventory = String.valueOf(itemsRemaining);
            
            Inventory changeAmount = new Inventory(nameOfItem);
            changeAmount.setItemCost(priceOfItem);
            changeAmount.setNumberOfItemsInInventory(numberOfItemsInInventory);
            dao.addInventory(changeAmount.getItemName(), changeAmount);
            dao.writeInventory();            
            
            //dao.writeInventory();
        return itemsRemaining;
    }

    
} // -- VendingMachineServiceLayerImpl --

