package com.example.trackier_library.dynamic_link

class AndroidParameters private constructor(val redirectLink: String) {
    class Builder {
        private var redirectLink: String = ""

        fun setRedirectLink(redirectLink: String) = apply { this.redirectLink = redirectLink }
        fun build() = AndroidParameters(redirectLink)
    }
}

class IosParameters private constructor(val redirectLink: String) {
    class Builder {
        private var redirectLink: String = ""

        fun setRedirectLink(redirectLink: String) = apply { this.redirectLink = redirectLink }
        fun build() = IosParameters(redirectLink)
    }
}

class DesktopParameters private constructor(val redirectLink: String) {
    class Builder {
        private var redirectLink: String = ""

        fun setRedirectLink(redirectLink: String) = apply { this.redirectLink = redirectLink }
        fun build() = DesktopParameters(redirectLink)
    }
}

class SocialMetaTagParameters private constructor(
    val title: String,
    val description: String,
    val imageLink: String
) {
    class Builder {
        private var title: String = ""
        private var description: String = ""
        private var imageLink: String = ""

        fun setTitle(title: String) = apply { this.title = title }
        fun setDescription(description: String) = apply { this.description = description }
        fun setImageLink(imageLink: String) = apply { this.imageLink = imageLink }
        fun build() = SocialMetaTagParameters(title, description, imageLink)
    }
}