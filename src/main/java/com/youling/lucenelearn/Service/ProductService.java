package com.youling.lucenelearn.Service;

import com.youling.lucenelearn.entity.Product;
import org.apache.lucene.search.Query;

import java.util.List;

public interface ProductService {

    void add(Product product);

    void update(Product product);

    List<Product> queryAll(Query query,Integer pageNumber,Integer pageSize);
}
