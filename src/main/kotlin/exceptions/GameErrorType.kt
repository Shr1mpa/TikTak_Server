package com.example.exceptions

import io.ktor.http.HttpStatusCode

enum class GameErrorType(val code: Int, val statusCode: HttpStatusCode) {
    GAME_ALREADY_FINISHED(1001, HttpStatusCode.Conflict),
    NOT_YOUR_TURN(1002, HttpStatusCode.Conflict),
    CELL_ALREADY_TAKEN(1003, HttpStatusCode.Conflict),
    SESSION_NOT_FOUND(1004, HttpStatusCode.NotFound),
    MISSING_SESSION_ID(1005, HttpStatusCode.BadRequest),
    INVALID_PLAYER(1006, HttpStatusCode.BadRequest),
    INVALID_CELL(1007, HttpStatusCode.BadRequest),
    INVALID_BODY(1008, HttpStatusCode.BadRequest),
    EMPTY_NAME(1009, HttpStatusCode.BadRequest),
    SESSION_FULL(1010, HttpStatusCode.Conflict),
    INTERNAL_ERROR(1500, HttpStatusCode.InternalServerError),
}