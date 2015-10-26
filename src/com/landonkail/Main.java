package com.landonkail;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        User user = new User();
        ArrayList<UserInput> posts = new ArrayList<>();
        Spark.staticFileLocation("/public");
        Spark.init();
        Spark.post(
            "/create-name",
                ((request, response) -> {
                    user.name = request.queryParams("username");
                    // user.password = request.queryParams("password");
                    response.redirect("/posts");
                    return "";
                })

        );
        Spark.post(
                "/user-input",
                ((request, response) -> {
                    UserInput userInput = new UserInput();
                    userInput.message = request.queryParams("message");
                    posts.add(userInput);
                    response.redirect("/posts");
                    return "";
                })
        );
        Spark.get(
                "/posts",
                ((request, response) -> {
                    UserInput userInput = new UserInput();
                    HashMap m = new HashMap();
                    m.put("name", user.name);
                    m.put("posts", posts);
                    return new ModelAndView(m, "posts.html");
                }),
                new MustacheTemplateEngine()
        );

    }
}
