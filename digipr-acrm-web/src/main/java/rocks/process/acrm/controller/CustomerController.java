/*
 * Copyright (c) 2020. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.acrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/customer")
public class CustomerController {

    @GetMapping
    public String getCustomerView(){
        return "customer.html";
    }

    @GetMapping("/create")
    public String getCustomerCreateView(){
        return "customerCreate.html";
    }

    @GetMapping("/edit")
    public String getCustomerEditView(){
        return "customerEdit.html";
    }
}
