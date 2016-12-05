//
//  WebserviceError.swift
//  Videos
//
//  Created by Florian on 13/04/16.
//  Copyright © 2016 Chris Eidhof. All rights reserved.
//

import Foundation

public enum WebserviceError: Error {
    case notAuthenticated
    case other
}

func logError<A>(_ result: Result<A>) {
    guard case let .error(e) = result else { return }
    assert(false, "\(e)")
}
