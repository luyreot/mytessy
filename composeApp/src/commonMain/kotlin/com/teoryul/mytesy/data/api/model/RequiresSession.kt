package com.teoryul.mytesy.data.api.model

/**
 * Certain api calls will return the following body indicating session has expired.
 * This marker interface is used to identify requests that need a valid session by an interceptor
 * which will be performing silent logins when session has expired.
 * {
 *   "login" : {
 *     "user" : "<user email address>",
 *     "msg" : "Invalid credentials",
 *     "error" : "1"
 *   }
 * }
 */
interface RequiresSession