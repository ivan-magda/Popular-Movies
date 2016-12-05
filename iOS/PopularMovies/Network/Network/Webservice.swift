//
//  Webservice.swift
//  Videos
//
//  Created by Florian on 13/04/16.
//  Copyright © 2016 Chris Eidhof. All rights reserved.
//

import UIKit

public typealias JSONDictionary = [String: Any]

// We create our own session here without a URLCache to avoid playground error messages
private var session: URLSession {
    let config = URLSessionConfiguration.default
    config.urlCache = nil
    return URLSession(configuration: config)
}

public final class Webservice {
    public var authenticationToken: String?
    public init() { }
    
    /// Loads a resource. The completion handler is always called on the main queue.
    public func load<A>(_ resource: Resource<A>, completion: @escaping (Result<A>) -> () = logError) {
        session.dataTask(with: resource.url, completionHandler: { data, response, _ in
            let result: Result<A>
            if let httpResponse = response as? HTTPURLResponse , httpResponse.statusCode == 401 {
                result = Result.error(WebserviceError.notAuthenticated)
            } else {
                let parsed = data.flatMap(resource.parse)
                result = Result(parsed, or: WebserviceError.other)
            }
            DispatchQueue.main.async { completion(result) }
        }) .resume()
    }
}
