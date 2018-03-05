package com.example.demo.controller;

import com.example.demo.model.Item;
import com.example.demo.model.User;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RequestMapping("/items")
@Controller
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    final private String itemDir = "model/item/";

    // PUBLIC
    @GetMapping("/search")
    public String getSearch() {
        return itemDir + "searchForm";
    }

    // PUBLIC
    @PostMapping("/search")
    public String showSearch(HttpServletRequest request, Model model) {
        String searchString = request.getParameter("search");
        model.addAttribute("search", searchString);
        if (searchString.equalsIgnoreCase("CLOTHES") || searchString.equalsIgnoreCase("PETS") || searchString.equalsIgnoreCase("OTHER")){
            model.addAttribute("items", itemRepository.findItemsByCategoryContainingIgnoreCase(searchString));
        }else{
            model.addAttribute("items",itemRepository.findItemsByTitleContainingIgnoreCase(searchString));
        }
        model.addAttribute("funName", "");
        return itemDir + "list";
    }

    // Public
    @RequestMapping("/lost")
    public String lostItemsList(Model model) {
        model.addAttribute("items", itemRepository.findByLostTrue());
        model.addAttribute("funName", "lostItemsList");
        return itemDir + "list";
    }

    // Public
    @PostMapping("/lost")
    public String lostItemsCategory(Model model, HttpServletRequest request) {
        String category = request.getParameter("category");
        Set<Item> items = new HashSet<>();
        for(Item item : itemRepository.findByLostTrue()){
            if(item.getCategory().equalsIgnoreCase(category)){
                items.add(item);
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("funName", "lostItemsList");
        return itemDir + "list";
    }


    // Public
    @RequestMapping("/found")
    public String foundItemsList(Model model) {
        model.addAttribute("items", itemRepository.findByLostFalse());
        model.addAttribute("funName", "foundItemsList");
        return itemDir + "list";
    }

    // User
    @RequestMapping("/userFound")
    public String userFoundItemsList(Model model, Authentication auth) {
        User user = userRepository.findUserByUsername(auth.getName());
        HashSet<Item> found = new HashSet<>();
        for (Item item : user.getItems()) {
            if (item.isLost() == false) {
                found.add(item);
            }
        }
        model.addAttribute("items", found);
        model.addAttribute("funName", "userFoundItemsList");
        return itemDir + "list";
    }

    // Public
    @RequestMapping("/detail/{id}")
    public String itemDetail(@PathVariable("id") long id, Model model) {
        Item item = itemRepository.findOne(id);
        model.addAttribute("item", item);
        model.addAttribute("image", item.getImage());
        return itemDir + "detail";
    }

    // Admin
    @RequestMapping()
    public String itemsList(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("funName", "itemsList");
        return itemDir + "list";
    }

    // Admin
    @RequestMapping("/update/{id}")
    public String updateItemStatus(@PathVariable("id") long id, Model model) {
        Item item = itemRepository.findOne(id);
        item.setLost(!item.isLost());
        itemRepository.save(item);
        return "redirect:/items";
    }

    // Admin or User
    @GetMapping("/add")
    public String itemForm(Model model, Authentication auth) {
        model.addAttribute("item", new Item());
        User user = userRepository.findUserByUsername(auth.getName());
        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            model.addAttribute("users", userRepository.findUsersByRole("USER"));
        }
        return itemDir + "form";
    }

    //Admin or User
    @PostMapping("/process")
    public String processForm(@Valid Item item, BindingResult result, Authentication auth) {
        if (result.hasErrors()) {
            return itemDir + "form";
        }
        User user = userRepository.findUserByUsername(auth.getName());
        if (user.getRole().equalsIgnoreCase("USER")) {
            item.addUser(user);
            itemRepository.save(item);
            user.addItem(item);
            userRepository.save(user);
        } else if (user.getRole().equalsIgnoreCase("ADMIN")) {
            if (userRepository.findUserByUsername(item.getOwner()) == null) {
                itemRepository.save(item);
            } else {
                User itemUser = userRepository.findUserByUsername(item.getOwner());
                item.addUser(itemUser);
                itemUser.addItem(item);
                itemRepository.save(item);
                userRepository.save(itemUser);
            }
        }

        return "redirect:" + "/items/lost";
    }


}
