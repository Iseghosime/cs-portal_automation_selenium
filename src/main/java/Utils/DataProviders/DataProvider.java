package Utils.DataProviders;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataProvider {
    public static Properties config = tests.BaseTest.config;

    @org.testng.annotations.DataProvider
    public Object[][] getTestData() {
        TestDataExcelToBeanDao credsExcelToBeanDao = new TestDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<TestDatabean> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("LoginSheet"));
        Object[][] hashMapObj = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            hashMapObj[i][0] = list.get(i);
        }
        return hashMapObj;
    }

    @org.testng.annotations.DataProvider
    public Object[][] getTestData1() {
        ftrDataExcelToBeanDao credsExcelToBeanDao = new ftrDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<ftrDataBeans> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("FtrSheet"));

        Object[][] hashMapObj = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            hashMapObj[i][0] = list.get(i);
        }
        return hashMapObj;
    }

    @org.testng.annotations.DataProvider
    public Object[][] getTestData2() {
        nftrDataExcelToBeanDao credsExcelToBeanDao = new nftrDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<nftrDataBeans> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("NftrSheet"));

        Object[][] hashMapObj = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            hashMapObj[i][0] = list.get(i);
        }
        return hashMapObj;
    }

    @org.testng.annotations.DataProvider
    public Object[][] getInteractionChannelData() {
        UMDataExcelToBeanDao credsExcelToBeanDao = new UMDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<UMDataBeans> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("InteractionChannelSheet"));
        Object[][] hashMapObj = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            hashMapObj[i][0] = list.get(i);
        }
        return hashMapObj;
    }

    @org.testng.annotations.DataProvider
    public Object[][] getLoginQueueData() {
        UMDataExcelToBeanDao credsExcelToBeanDao = new UMDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<UMDataBeans> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("LoginQueueSheet"));
        Object[][] hashMapObj = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            hashMapObj[i][0] = list.get(i);
        }
        return hashMapObj;
    }

    @org.testng.annotations.DataProvider
    public Object[][] getWorkFlowData() {
        UMDataExcelToBeanDao credsExcelToBeanDao = new UMDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<UMDataBeans> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("WorkFlowsSheet"));
        Object[][] hashMapObj = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            hashMapObj[i][0] = list.get(i);
        }
        return hashMapObj;
    }

    @org.testng.annotations.DataProvider
    public Object[][] getSingleRow(Method method) {
        PinnedTagDataExcelToBeanDao credsExcelToBeanDao = new PinnedTagDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        RowNumber rows = method.getAnnotation(RowNumber.class);
        List<PinnedtagsDataBeans> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("PinnedTagSheet"));
        Object[][] hashMapObj = new Object[1][1];
        if (rows == null) {
            for (int i = 0; i < list.size(); i++) {
                hashMapObj[i][0] = list.get(i);
            }
            return hashMapObj;
        }
        int whichRow = Integer.parseInt(rows.rowNumber());
        hashMapObj[0][0] = list.get(whichRow - 1);
        return hashMapObj;

    }

    @org.testng.annotations.DataProvider(name = "ticketState")
    public Object[][] ticketStateList() {
        TicketStateToBean ticketStateToBean = new TicketStateToBean();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<TicketStateDataBean> list =
                ticketStateToBean.getData(Excel.getAbsolutePath(), config.getProperty("ticketState"));
        List<TicketStateDataBean> closeState = new ArrayList<TicketStateDataBean>();
        List<TicketStateDataBean> openState = new ArrayList<TicketStateDataBean>();
        for (TicketStateDataBean state : list) {
            if (state.getInternalState().equals(config.getProperty("closeState"))) {
                closeState.add(state);
            } else {
                openState.add(state);
            }
        }
        Object[][] hashMapObj = new Object[1][1];

        hashMapObj[0][0] = closeState.get(0);

        return hashMapObj;
    }

    @org.testng.annotations.DataProvider(name = "ticketId")
    public Object[][] getTestData5() {
        nftrDataExcelToBeanDao credsExcelToBeanDao = new nftrDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        List<nftrDataBeans> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("NftrSheet"));
        List<nftrDataBeans> finalTicketList = new ArrayList<nftrDataBeans>();
        for (nftrDataBeans nftrTicket : list) {
            System.out.println("Ticket Id: " + nftrTicket.getTicketNumber());
            if (nftrTicket.getTicketNumber() == null) {
                System.out.println("No Ticket ID Found");
            } else {
                finalTicketList.add(nftrTicket);
            }
        }

        Object[][] hashMapObj = new Object[finalTicketList.size()][1];
        for (int i = 0; i < finalTicketList.size(); i++) {
            hashMapObj[i][0] = finalTicketList.get(i);
        }
        return hashMapObj;
    }

    @org.testng.annotations.DataProvider(name = "loginData")
    public Object[][] getLoginData(Method method) {
        TestDataExcelToBeanDao credsExcelToBeanDao = new TestDataExcelToBeanDao();
        File Exceldir = new File("Excels");
        File Excel = new File(Exceldir, tests.BaseTest.Opco + ".xlsx");
        User rows = method.getAnnotation(User.class);
        List<TestDatabean> list =
                credsExcelToBeanDao.getData(Excel.getAbsolutePath(), config.getProperty("LoginSheet"));
        List<TestDatabean> finalTicketList = new ArrayList<>();
        for (TestDatabean login : list) {
            System.out.println("User Type " + login.getUserType());
            if (login.getUserType().toLowerCase().trim().equals(rows.UserType().toLowerCase().trim())) {
                finalTicketList.add(login);
                System.out.println("User Type ADDED " + login.getUserType());

            } else {
                System.out.println("NO USER FOUND");
            }
        }
        Object[][] hashMapObj = new Object[finalTicketList.size()][1];
        for (int i = 0; i < finalTicketList.size(); i++) {
            hashMapObj[i][0] = finalTicketList.get(i);
        }
        return hashMapObj;
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface RowNumber {
        String rowNumber() default "";
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface User {
        String UserType() default "";
    }
}
