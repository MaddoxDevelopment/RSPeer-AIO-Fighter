package org.rspeer.wrappers;

import org.rspeer.runetek.api.component.Bank;

public class BankWrapper {

    public static boolean openNearest() {
        if(Bank.isOpen()) {
            return true;
        }
        return Bank.open();
    }

}
