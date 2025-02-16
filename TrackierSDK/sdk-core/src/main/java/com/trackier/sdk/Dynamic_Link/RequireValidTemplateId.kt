package com.example.trackier_library.annotations

@Retention(AnnotationRetention.BINARY) // Make it enforceable at compile time
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
annotation class RequireValidTemplateId




