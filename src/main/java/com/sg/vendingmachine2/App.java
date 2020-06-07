/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine2;

import com.sg.vendingmachine2.controller.VendingMachineController;
import com.sg.vendingmachine2.dao.VendingMachineAuditDao;
import com.sg.vendingmachine2.dao.VendingMachineAuditDaoFileImpl;
import com.sg.vendingmachine2.dao.VendingMachineDao;
import com.sg.vendingmachine2.dao.VendingMachineDaoFileImpl;
import com.sg.vendingmachine2.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine2.dto.Change;
import com.sg.vendingmachine2.service.VendingMachineInsufficientFundsException;
import com.sg.vendingmachine2.service.VendingMachineNoItemInventoryException;
import com.sg.vendingmachine2.service.VendingMachineServiceLayer;
import com.sg.vendingmachine2.service.VendingMachineServiceLayerImpl;
import com.sg.vendingmachine2.ui.UserIO;
import com.sg.vendingmachine2.ui.UserIOConsoleImpl;
import com.sg.vendingmachine2.ui.VendingMachineView;

/**
 *
 * @author travi
 */
public class App {
    public static void main(String[] args) throws VendingMachinePersistenceException, 
            VendingMachineNoItemInventoryException, VendingMachineInsufficientFundsException {
        
        UserIO myIO = new UserIOConsoleImpl();
        Change myChange = new Change();
        VendingMachineView myView = new VendingMachineView(myIO, myChange);
        VendingMachineDao myDao = new VendingMachineDaoFileImpl();
        VendingMachineAuditDao myAuditDao = new VendingMachineAuditDaoFileImpl();
        VendingMachineServiceLayer myService = 
                new VendingMachineServiceLayerImpl(myDao, myAuditDao);
        VendingMachineController controller = 
                new VendingMachineController(myService, myView);        
       controller.run();
       controller.productMenu();
        
    }
}
