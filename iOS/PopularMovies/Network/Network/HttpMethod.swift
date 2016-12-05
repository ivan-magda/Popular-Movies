//
//  HttpMethod.swift
//  Videos
//
//  Created by Florian on 13/04/16.
//  Copyright © 2016 Chris Eidhof. All rights reserved.
//

import Foundation

public enum HttpMethod<A> {
    case get
    case post(data: A)
    
    public var method: String {
        switch self {
        case .get: return "GET"
        case .post: return "POST"
        }
    }
    
    public func map<B>(f: (A) throws -> B) rethrows -> HttpMethod<B> {
        switch self {
        case .get: return .get
        case .post(let data): return .post(data: try f(data))
        }
    }
}
