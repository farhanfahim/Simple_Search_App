package com.app.simplesearch.data.model

class APIError(
    var message: String?,
    var statusCode: Int?
) {
    constructor() : this(null, -0)
}