I often have to work with Spring MVC based code bases that have been living for a while. One of the common trait of these code bases I have observed is the repeated use of the same Spring annotations to achieve the same effect again and again. A really good example of this is annotating a controller method with a combination of @RequestMapping and @ResponseStatus to set the expected request and response Content-Type, the request Method, response status etc. This blog post describes how to create custom Spring annotationshow to create custom annotation in spring mvc that produce the same effect as multiple Spring annotations acting together.

Among the new things introduced in Spring 4 was the idea of meta-annotations. 1 Meta annotations are annotations that can act up on other annotations by modifying and overriding attributes of the target annotations.

This allows us to build composed annotations 2 that combine the behavior of multiple annotations. The @AliasFor annotation allows us to override attribute names, thereby adding great flexibility while composing.

This is a common pattern in code bases that use Spring MVC.

    @RequestMapping(path = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Person register(@RequestBody Person person) {
        return registrationService.register(person);
    }

The two annotations as applied on this method represent the fact that this is an end point that a client can POST JSON to and produces JSON and a 201 Created HTTP status when it succeeds. If we had 20 end points that did the similar operations, it would be really useful to have a @PostJson annotation that did the same thing.

The following is one way of writing such an annotation.

@Target(METHOD)
@Retention(RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@ResponseStatus(HttpStatus.CREATED)
public @interface PostJson {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] path() default {};
}

This annotation takes a single parameter path that represents that URL path to which it will respond to. This can now be applied on the register end point.

    @PostJson(path = "/register")
    public Person register(@RequestBody Person person) {
        return registrationService.register(person);
    }

If we wanted to allow the consumers of @PostJson to override other parameters of @RequestMapping or @ResponseStatus, it can be achieved by adding more aliases. For example, the following will allow users to specify a reason phrase.

    @AliasFor(annotation = ResponseStatus.class, attribute = "reason")
    String[] reason() default {};
