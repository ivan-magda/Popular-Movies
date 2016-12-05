//
//  Result.swift
//  Videos
//
//  Created by Florian on 13/04/16.
//  Copyright © 2016 Chris Eidhof. All rights reserved.
//

import Foundation

public enum Result<A> {
    case success(A)
    case error(Error)
}

extension Result {
    public init(_ value: A?, or error: Error) {
        if let value = value {
            self = .success(value)
        } else {
            self = .error(error)
        }
    }
    
    public var value: A? {
        guard case .success(let v) = self else { return nil }
        return v
    }
}
