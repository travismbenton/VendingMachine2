/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine2.controller;

import com.sg.vendingmachine2.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine2.dto.Inventory;
import com.sg.vendingmachine2.service.VendingMachineDataValidationException;
import com.sg.vendingmachine2.service.VendingMachineDuplicateItemException;
import com.sg.vendingmachine2.service.VendingMachineInsufficientFundsException;
import com.sg.vendingmachine2.service.VendingMachineNoItemInventoryException;
import com.sg.vendingmachine2.service.VendingMachineServiceLayer;
import com.sg.vendingmachine2.ui.VendingMachineView;
import java.util.List;

/**
 *
 * @author travi
 */
public class VendingMachineController {    
    
    VendingMachineView view; 
    VendingMachineServiceLayer service;
    
     // -- CONSTRUCTOR --
    public VendingMachineController(VendingMachineServiceLayer service, VendingMachineView view){
        this.service = service;
        this.view = view;
    }
    // -- "END" CONSTRUCTOR --
    
    public void run() throws VendingMachinePersistenceException {
        
        int menuSelection = 0;
        boolean keepGoing = true;
        try {
        while (keepGoing){
            
                menuSelection = getRunMenuSelection();
            
            switch (menuSelection){
                case 1:
                    add();
                    break;
                case 2:
                    edit();
                    break;
                case 3:
                    listAll();
                    break;
                case 4:
                    find();
                    break;
                case 5:
                    remove();
                    break;                
                case 6:
                    keepGoing = false;
                    break;
                case 7:
                    unknownCommand();
                    break;
     
            } // -- "END" SWITCH --            
           
            
        } // -- "END" WHILE LOOP --
        exitRunMessage();
        
        } catch (VendingMachinePersistenceException | NumberFormatException e){
            run();
        }        
                   
            
    } // -- "END" RUN --      
    
    //---------------------------------------------------------|
    
    //---------------------------------------------------------|    
        
    public void productMenu() throws VendingMachinePersistenceException, 
            VendingMachineNoItemInventoryException, VendingMachineInsufficientFundsException {
        
        int menuSelection;
        boolean keepGoing = true;
        try {
        while (keepGoing){
            
            menuSelection = getProductMenuSelection();
            
            switch (menuSelection){
                case 1:
                    chips();
                    break;
                case 2:
                    candyBar();
                    break;
                case 3:
                    soda();
                    break;
                case 4:
                    sandwich();
                    break;
                case 5:
                    cookies();
                    break;            
                case 6:
                    keepGoing = false;
                    break;
                case 7:
                    unknownCommand();
                    break;
                     
            } // -- "END" SWITCH --            
           
            
        } // -- "END" WHILE LOOP --
        exitProductMessage();
        
        } catch (VendingMachinePersistenceException | NumberFormatException |
                VendingMachineNoItemInventoryException e){
            
            view.displayProductErrorMessage(e.getMessage());
            productMenu();            
        }        
                  
            
    } // -- "END" PRODUCT MENU --    
    
    //---------------------------------------------------------|
    
    // -- Menu Section --    
    public int getProductMenuSelection() throws VendingMachinePersistenceException{
        return view.printProductMenuGetUserSelection();
    }
    public int getRunMenuSelection() throws VendingMachinePersistenceException{
        return view.printRunMenuGetUserSelection();
    }    
    private void unknownCommand(){
        view.displayUnknownMenuBanner();
    }        
    private void exitProductMessage(){
        view.displayExitMenuBanner();
    } 
    private void exitRunMessage(){
        view.displayExitRunMenuBanner();
    } 
    // -- "END" Menu Section --
    
    //---------------------------------------------------------| 
    
    // ---------------------------------------------|  
    
    // -- ADD  SECTION --    
    private void add() throws VendingMachinePersistenceException{
        view.displayAddInventoryBanner();
        boolean hasErrors = false;
        do {
        Inventory newInventory = view.getNewInventoryInfo();
            try {
                service.createInventory(newInventory);
                view.displayAddInventorySuccessfulBanner();
                hasErrors = false;
           } catch (VendingMachineDuplicateItemException | 
                    VendingMachineDataValidationException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
           }
               
       } while (hasErrors);
    }           
    // -- "END" ADD  SECTION --
    
    //---------------------------------------------------------|      
        
    // -- EDIT  SECTION --    
    private void edit()throws VendingMachinePersistenceException {        
        view.displayEditInventoryBanner();
        boolean hasErrors = false;
        do {
            Inventory currentInventory = view.getNewInventoryInfo();
        try {
            service.editInventory(currentInventory);
            view.displayEditInventorySuccessfulBanner();
                hasErrors = false;
        } catch (VendingMachineDataValidationException e) {                    
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
           }
               
       } while (hasErrors);
        
    }         
    // -- "END" EDIT  SECTION --
    
    //---------------------------------------------------------|    
        
    // -- LIST  SECTION --    
    private void listAll() throws VendingMachinePersistenceException{
        view.displayListAllInventoryBanner();
        List<Inventory> inventoryList = service.getAllInventory();
        view.listAllInventory(inventoryList);
    }
    // -- "END" LIST  SECTION --
    
    //---------------------------------------------------------|
    
    // -- FIND  SECTION --    
    private void find() throws VendingMachinePersistenceException{
        view.displayFindItemBanner();
        String item = view.getItemChoice();
        Inventory newItem = service.getInventory(item);
        view.displayItem(newItem);    
    }        
    // -- "END" FIND  SECTION --
    
    //---------------------------------------------------------| 
        
    // -- REMOVE  SECTION --         
    private void remove() throws VendingMachinePersistenceException{
        String itemName = view.getItemChoiceToRemove();
        Inventory item = service.getInventory(itemName);
        
        view.displayItemToRemove(item);
        if (item != null){
        service.removeInventory(itemName);
        view.displayRemoveItemSuccessBanner();
        } else {
          // intentionally left blank
      }
    }    
    // -- "END" REMOVE  SECTION --    
       
    //---------------------------------------------------------|     
    
    
    //---------------------------------------------------------|
    
    // -- PRODUCT SECTION --
    
    //---------------------------------------------------------|    
    
    
    //---------------------------------------------------------| 

    
    // -- Chips  SECTION --    
    private void chips() throws VendingMachinePersistenceException, 
                                VendingMachineNoItemInventoryException, 
                                VendingMachineInsufficientFundsException {   
        
        String item = "Chips";
        Inventory newItem = service.getInventory(item);
        String nameOfItem = view.displayItemName(newItem);
        String numberOfItems = view.displayItemsRemaining(newItem);        
        Integer itemsRemaining = Integer.valueOf(numberOfItems);
        System.out.println("Current amount: "+itemsRemaining);
        String priceOfItem = view.displayItemPrice(newItem);
        double newItemPrice = Double.valueOf(priceOfItem);
        
        
        service.validateItemInInventory(itemsRemaining, nameOfItem);          
       
        
        double itemPrice = newItemPrice;
        double userMoney=0;
        boolean hasErrors = false;
        do {                
            try {
                userMoney = service.validatePrice(itemPrice);
                
                hasErrors = false;
            } catch (VendingMachineInsufficientFundsException e) {                      
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());                
            }
        } while (hasErrors); 
        view.changeCalculations(userMoney, itemPrice);
        service.auditLog(itemsRemaining, nameOfItem, priceOfItem);
        
    }           
    // -- "END" Chips  SECTION --
    
    //---------------------------------------------------------|      
        
    // -- Candy Bar  SECTION --    
    private void candyBar() throws VendingMachinePersistenceException,
                                   VendingMachineNoItemInventoryException, 
                                   VendingMachineInsufficientFundsException {
        String item = "Candy Bar";
        Inventory newItem = service.getInventory(item);
        String nameOfItem = view.displayItemName(newItem);
        String numberOfItems = view.displayItemsRemaining(newItem);
        Integer itemsRemaining = Integer.valueOf(numberOfItems);
        String priceOfItem = view.displayItemPrice(newItem);
        double newItemPrice = Double.valueOf(priceOfItem);
        
        
        service.validateItemInInventory(itemsRemaining, nameOfItem);    
        
        
        double itemPrice = newItemPrice;
        double userMoney=0;
        boolean hasErrors = false;
        do {                
            try {
                userMoney = service.validatePrice(itemPrice);
                
                hasErrors = false;
            } catch (VendingMachineInsufficientFundsException e) { 
                     
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());                
            }
        } while (hasErrors);         
        view.changeCalculations(userMoney, itemPrice);
        service.auditLog(itemsRemaining, nameOfItem, priceOfItem);
        
    }         
    // -- "END" Candy Bar  SECTION --
    
    //---------------------------------------------------------|    
        
    // -- Soda  SECTION --    
    private void soda() throws VendingMachinePersistenceException,
                               VendingMachineNoItemInventoryException, 
                               VendingMachineInsufficientFundsException {
        String item = "Soda";
        Inventory newItem = service.getInventory(item);
        String nameOfItem = view.displayItemName(newItem);
        String numberOfItems = view.displayItemsRemaining(newItem);
        Integer itemsRemaining = Integer.valueOf(numberOfItems);
        String priceOfItem = view.displayItemPrice(newItem);
        double newItemPrice = Double.valueOf(priceOfItem);
        
        
        service.validateItemInInventory(itemsRemaining, nameOfItem);   
        
        
        double itemPrice = newItemPrice;
        double userMoney=0;
        boolean hasErrors = false;
        do {                
            try {
                userMoney = service.validatePrice(itemPrice);
                
                hasErrors = false;
            } catch (VendingMachineInsufficientFundsException e) { 
                     
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());                
            }
        } while (hasErrors); 
        view.changeCalculations(userMoney, itemPrice);
        service.auditLog(itemsRemaining, nameOfItem, priceOfItem);
    
    }
    // -- "END" Soda  SECTION --
    
    //---------------------------------------------------------|
    
    // -- Sandwich  SECTION --    
    private void sandwich() throws VendingMachinePersistenceException,
                                   VendingMachineNoItemInventoryException, 
                                   VendingMachineInsufficientFundsException {
        String item = "Sandwich";
        Inventory newItem = service.getInventory(item);
        String nameOfItem = view.displayItemName(newItem);
        String numberOfItems = view.displayItemsRemaining(newItem);
        Integer itemsRemaining = Integer.valueOf(numberOfItems);
        String priceOfItem = view.displayItemPrice(newItem);
        double newItemPrice = Double.valueOf(priceOfItem);
        
        
        service.validateItemInInventory(itemsRemaining, nameOfItem);  
        
        
        double itemPrice = newItemPrice;
        double userMoney=0;
        boolean hasErrors = false;
        do {                
            try {
                userMoney = service.validatePrice(itemPrice);
                
                hasErrors = false;
            } catch (VendingMachineInsufficientFundsException e) { 
                     
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());                
            }
        } while (hasErrors); 
        view.changeCalculations(userMoney, itemPrice);
        service.auditLog(itemsRemaining, nameOfItem, priceOfItem);
       
    }        
    // -- "END" Sandwich  SECTION --
    
    //---------------------------------------------------------|
    
    // -- Cookies  SECTION --    
    private void cookies() throws VendingMachinePersistenceException,
                                   VendingMachineNoItemInventoryException, 
                                   VendingMachineInsufficientFundsException {
        String item = "Cookies";
        Inventory newItem = service.getInventory(item);
        String nameOfItem = view.displayItemName(newItem);
        String numberOfItems = view.displayItemsRemaining(newItem);
        Integer itemsRemaining = Integer.valueOf(numberOfItems);
        String priceOfItem = view.displayItemPrice(newItem);
        double newItemPrice = Double.valueOf(priceOfItem);
        
        
        service.validateItemInInventory(itemsRemaining, nameOfItem); 
        
        
        double itemPrice = newItemPrice;
        double userMoney=0;
        boolean hasErrors = false;
        do {                
            try {
                userMoney = service.validatePrice(itemPrice);
                
                hasErrors = false;
            } catch (VendingMachineInsufficientFundsException e) { 
                     
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());                
            }
        } while (hasErrors); 
        view.changeCalculations(userMoney, itemPrice);
        service.auditLog(itemsRemaining, nameOfItem, priceOfItem);
    
    }        
    // -- "END" Cookies  SECTION --
    
    //---------------------------------------------------------| 

    
    
    
    
    
    
    
    
    
    
    
    
    

}    