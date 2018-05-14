package com.youling.lucenelearn.repository;

import com.youling.lucenelearn.Service.ProductService;
import com.youling.lucenelearn.entity.Category;
import com.youling.lucenelearn.entity.Product;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.text.DecimalFormat;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    public void testSave(){
        for (int i = 1; i <= 5; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName("类目"+i);
            categoryRepository.save(category);
        }
    }

    @Test
    public void testSave1(){
        for (int i = 10; i <= 10; i++) {
            Product product = new Product();
            product.setId(i);
            product.setCategoryId(1);
            product.setLocation("产地"+i);
            product.setName("好利来"+i);
            //生成100以内的随机两位浮点数
            DecimalFormat df = new DecimalFormat("#.00");
            String s = df.format(Math.random() * 100);
            double d = Double.parseDouble(s);
            product.setPrice(d);
            productService.add(product);
        }
    }

    @Test
    public void testRandom(){

        DecimalFormat df = new DecimalFormat("#.00");
        String s = df.format(Math.random() * 100);
        double d = Double.parseDouble(s);
        System.out.println(d);
    }

    @Test
    public void testQueryAll() throws ParseException {
        Integer pageNumber=null;
        Integer pageSize=null;
        //pageNumber=2;
        MultiFieldQueryParser multiFieldQueryParser =
                new MultiFieldQueryParser(Version.LUCENE_44, new String[]{"name"}, new IKAnalyzer());
        Query query = multiFieldQueryParser.parse("我要买好利来蛋糕");
        List<Product> productList = productService.queryAll(query,pageNumber,pageSize);
        System.out.println(productList);
    }

    @Test
    public void testUpdate(){
        //for (int i = 7; i <= 9; i++) {
            Product product = new Product();
            product.setId(10);
            product.setCategoryId(1);
            product.setLocation("产地"+10);
            product.setName("鸡蛋糕"+10);
            //生成100以内的随机两位浮点数
            DecimalFormat df = new DecimalFormat("#.00");
            String s = df.format(Math.random() * 100);
            double d = Double.parseDouble(s);
            product.setPrice(d);
            productService.update(product);
       // }
    }
}