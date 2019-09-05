package com.davidluoye.support.test;

import com.davidluoye.support.log.ILogger;
import com.davidluoye.support.util.IType;
import com.davidluoye.support.util.ThreadUtil;

import java.lang.reflect.Method;
import java.util.List;

public class TestRunner {
    private static final ILogger LOGGER = ILogger.logger("TestRunner");

    public static void execute(String filterPackage) {
        execute(filterPackage, null);
    }

    public static void execute(String filterPackage, CaseResult result) {
        CasesExecutor executor = new CasesExecutor(filterPackage, result);
        ThreadUtil.execute(executor);
    }

    private static class CasesExecutor implements Runnable {
        private String filterPackage;
        private CaseResult result;
        public CasesExecutor(String filterPackage, CaseResult result) {
            this.result = result;
            if (this.result == null) {
                this.result = new CaseResult();
            }

            this.filterPackage = filterPackage;
        }

        @Override
        public void run() {
            List<Class> classList = TestScanner.scanTestClass(filterPackage);
            for (Class clazz : classList) {

                String parent = clazz.getName();
                CaseResult.TestClass testClass = new CaseResult.TestClass(parent);
                result.add(testClass);

                LOGGER.i("====== enter %s", clazz);

                Object classObj = null;
                try {
                    classObj = clazz.newInstance();
                } catch (Exception e) {
                    String msg = e.getMessage();
                    LOGGER.e("error, can not create instance: %s as %s", parent, msg);
                    continue;
                }

                TestUnit instance = null;
                try {
                    instance = IType.translate(classObj);
                } catch (Exception e) {
                    String msg = e.getMessage();
                    LOGGER.e("error, can not translate instance: %s as %s", parent, msg);
                    continue;
                }

                if (instance != null) {
                    instance.start();
                }

                List<Method> methods = TestScanner.scanTestCase(clazz);
                int index = 0;
                for (Method method : methods) {
                    String child = method.getName();
                    method.setAccessible(true);

                    CaseResult.TestCase tCase = new CaseResult.TestCase(parent, child);
                    testClass.add(tCase);

                    try {
                        index++;
                        LOGGER.i(" %s start %s#%s", index, clazz, child);
                        method.invoke(instance);
                        LOGGER.i(" >> success case: %s", child);
                        tCase.setSuccess(true);
                    } catch (Exception e) {
                        tCase.setSuccess(false);
                        LOGGER.i(" >> fail case: %s", child);
                        e.printStackTrace();
                    }
                }

                if (instance != null) {
                    instance.stop();
                }

                LOGGER.i("====== exit %s", clazz);
            }
        }
    }
}
