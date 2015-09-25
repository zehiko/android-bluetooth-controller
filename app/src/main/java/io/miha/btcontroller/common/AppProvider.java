package io.miha.btcontroller.common;

import io.miha.btcontroller.connector.BtFactory;
import io.miha.btcontroller.connector.BtUuidResolver;
import io.miha.btcontroller.connector.DialogFactory;
import io.miha.btcontroller.connector.SimpleDialogFactory;

public class AppProvider {

    private static final BtUuidResolver uuidResolverInstance = new BtUuidResolver();
    private static final BtContainer container = new BtContainer();
    private static final BtFactory btFactory = new BtFactory();
    private static final DialogFactory dialogFactory = new SimpleDialogFactory();

    public static BtUuidResolver getUuidResolver() {
        return uuidResolverInstance;
    }

    public static BtContainer getBtContainer() { return container; }

    public static BtFactory getBtFactory() {
        return btFactory;
    }

    public static DialogFactory getDialogFactory() {
        return dialogFactory;
    }
}
