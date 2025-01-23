package ui.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserRole {
        BUYER("Buyer"),
        CATEGORY_ASSISTANT("Category Assistant");

        @Getter
        private String role;
}
