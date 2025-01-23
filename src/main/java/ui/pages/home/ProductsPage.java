package ui.pages.home;

import ui.pages.base.BasePage;

import static com.codeborne.selenide.Selenide.$x;

public class ProductsPage extends BasePage {
    private final static String PRODUCTS_RESULTS = "//span[@class='results']";
    private final static String SUPPLIER_FILTER_ID = "Supplier";
    private final static String STATUS_FILTER_ID = "Status";
    private final static String TASK_FILTER_ID = "Task";
    private final static String PRODUCTS_GRID = "//div[@class='MuiContainer-root MuiContainer-maxWidthLg']";
    private final static String PRODUCTS_IN_THE_GRID = "//class='jss44'";


    public String verifyProductsResults() {
        return $x(PRODUCTS_RESULTS).getValue();
    }

    public ProductsPage filterProductsBySupplier() {
        return this;
    }

    public ProductsPage filterProductsByStatus() {
        return this;
    }


}
