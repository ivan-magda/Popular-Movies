package com.ivanmagda.popularmovies.network;

import java.net.URL;

/**
 * Helper wrapper class around necessary parameters for executing a network query.
 *
 * @param <A> The result type, after the invoking a network query.
 */
public final class Resource<A> {

    public interface Parse<Result> {
        public Result parse(String response);
    }

    public URL url;
    public String httpMethodName = "GET";
    public Parse<A> parseBlock;

    public Resource(URL url, Parse<A> parse) {
        this.url = url;
        this.parseBlock = parse;
    }

    public Resource(URL url, String httpMethodName, Parse<A> parse) {
        this.url = url;
        this.httpMethodName = httpMethodName;
        this.parseBlock = parse;
    }
}
