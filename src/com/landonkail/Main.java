package com.landonkail;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<UserInput> posts = new ArrayList<>();
        Spark.init();
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    UserInput userInput = new UserInput();
                    userInput.message = session.attribute("message");
                    String userName = session.attribute("username");
                    if (userName == null) {
                        return new ModelAndView(new HashMap<>(), "index.html");
                    }
                    HashMap m = new HashMap();
                    m.put("name", userName);
                    m.put("posts", posts);
                    return new ModelAndView(m, "posts.html");
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-name",
                ((request, response) -> {
                    String userName = request.queryParams("username");
                    Session session = request.session();
                    session.attribute("username", userName);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/user-input",
                ((request, response) -> {

                    Session session = request.session();
                    UserInput userInput = new UserInput();
                    userInput.message = request.queryParams("message");
                    userInput.id = posts.size() + 1;
                    session.attribute("message", userInput.message);
                    posts.add(userInput);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.remove(idNum -1);
                        for (int i =0; i < posts.size(); i++) {
                            posts.get(i).id = i + 1;
                        }
                    } catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/edit-post",
                ((request, response) -> {
                    String editPost = request.queryParams("editpost");
                    String postId = request.queryParams("editpostid");
                    try {
                        int postIdNum = Integer.valueOf(postId);
                        UserInput stuff = posts.get(postIdNum - 1);
                        stuff.message = editPost;
                    } catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );

}
}
