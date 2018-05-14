package com.youling.lucenelearn.controller;

import com.youling.lucenelearn.Service.ProductService;
import com.youling.lucenelearn.entity.Product;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.util.List;

@Controller
@RequestMapping("product")
public class productController {

    @Autowired
    private ProductService productService;

    @RequestMapping("list")
    public ModelAndView productList(String field, Integer pageNumber, Integer pageSize, Model model) throws ParseException {
        MultiFieldQueryParser multiFieldQueryParser =
                new MultiFieldQueryParser(Version.LUCENE_44, new String[]{"name"}, new IKAnalyzer());
        Query query = multiFieldQueryParser.parse(field);
        List<Product> productList = productService.queryAll(query, pageNumber, pageSize);
        model.addAttribute("productList", productList);
        return new ModelAndView("productList","productModel",model);
    }
}
