package com.teoryul.mytesy.domain.session

class UnauthenticatedException : IllegalStateException("No active session")