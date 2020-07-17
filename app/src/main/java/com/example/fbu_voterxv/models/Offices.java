package com.example.fbu_voterxv.models;

import androidx.annotation.NonNull;

public enum Offices {
     HOUSE_OF_REPRESENTATIVES{
         @NonNull
         @Override
         public String toString() {
             return "U.S House of Representatives";
         }
     },
    SENATE{
        @NonNull
        @Override
        public String toString() {
            return "U.S Senate";
        }
    },
    PRESIDENT{
        @NonNull
        @Override
        public String toString() {
            return "President of the United States";
        }
    },
    VICE_PRESIDENT{
        @NonNull
        @Override
        public String toString() {
            return "Vice President of the United States";
        }
    };
}
