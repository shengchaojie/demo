package com.scj.freemaker;

import com.scj.freemaker.model.Product;
import com.scj.freemaker.model.TestModel;
import freemarker.template.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by shengcj on 2016/8/12.
 */
public class TestMain {
    public static void main(String[] args) throws IOException, TemplateException {
        Configuration configuration =new Configuration(Configuration.VERSION_2_3_25);
        //configuration.setDirectoryForTemplateLoading(new File(TestMain.class.getClassLoader().getResource("ftl").getFile()));
        configuration.setClassLoaderForTemplateLoading(TestMain.class.getClassLoader(),"ftl");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        //System.out.println(TestMain.class.getClassLoader().getResource("ftl").getFile());

        TestModel testModel =new TestModel();
        testModel.setUser("scj");
        Product product =new Product();
        product.setName("xbox");
        product.setUrl("www.shengchaojie.com");
        testModel.setProduct(product);

        Template template =configuration.getTemplate("test.ftlh");

        Writer out =new OutputStreamWriter(System.out);
        template.process(testModel,out);

    }
}
