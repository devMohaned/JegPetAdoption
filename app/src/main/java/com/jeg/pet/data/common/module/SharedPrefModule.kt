package com.jeg.pet.data.common.module

import android.content.Context
import com.jeg.pet.data.common.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    // TODO: Change SharedPreference to Encrypted
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context) : SharedPrefs{
        return SharedPrefs(context)
    }
}

/*


1. User Authentication
-----------------------
    a. User log in
        - Request: Email & Password
        - Validation: Check Valid or Invalid Information
        - Response: +OBJECT(id,name,email,token)

    b. User Register
        - Request: FullName & Email & Password
        - Validation: Email Existence + Email Validation + Password Validation + Full Name Validation
        - Response: +OBJECT(id,name,email,token)

    c. User Confirmation Email
        - Request: Email
        - Validation: Email Existence
        - Response: Message With CHeck your email for information

        c.1 User Confirming The Token Sent
            - Request: Token
            - Validation: Check If Token is correct
            - Response: Result of token

    d. User Reset Password
        - Request: Email
        - Validation: Email Existence & Send Email to reset
        - Response: Message With CHeck your email for information

        d. #1 User Resetting Password by The Token Sent
            - Request: Token
            - Validation: Check If Token is correct
            - Response: Result of token

    e. User Update Password
        - Request:  Email & Old Password & New Password
        - Validation: Check Valid or Invalid Information
        - Response: +Message with Success or Failed


2. Pet Posting
----------------------


 - Response:
        {
            "code": 200, // NOT NULL
            "message": "Write_message_here", // NOT NULL
            "errors": [], // NULLABLE
            "data": {}, // NULLABLE
        }



 */