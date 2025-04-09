package fi.sulku.sulkumail.auth.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.ui.graphics.vector.ImageVector

// todo temp
//todo unread, and other uncommon folders
enum class Folder(val label: String, val icon: ImageVector, val contentDesc: String) {
    Inbox("INBOX", Icons.Rounded.Inbox, "desc"),
    Spam("SPAM", Icons.Rounded.Block, "desc"),
    Sent("SENT", Icons.AutoMirrored.Rounded.Send, "desc"),
    Drafts("DRAFT", Icons.Rounded.EditNote, "desc"),
    Trash("TRASH", Icons.Rounded.Delete, "desc")
}

/*
Inbox: INBOX
Trash: TRASH
Spam: SPAM
Sent: SENT
Drafts: DRAFT


Starred: STARRED
Important: IMPORTANT
All Mail: CATEGORY_PERSONAL (or ALL_MAIL for all emails)
Promotions: CATEGORY_PROMOTIONS
Social: CATEGORY_SOCIAL
Updates: CATEGORY_UPDATES
Forums: CATEGORY_FORUMS
 */



/*
Common Folders

Inbox: Where incoming emails are received.
Sent: Where sent emails are stored.
Drafts: Where unsent emails are saved.
Spam: Where suspected spam emails are stored.
Trash/Deleted: Where deleted emails are stored temporarily.

Gmail-Specific Folders

All Mail: Contains all emails, including those in the Inbox, Sent, and Archived.
Important: Emails marked as important by Gmail's algorithm.
Starred: Emails marked with a star for easy reference.
Chats: Contains chat messages if Google Chat is used.
Categories: Includes Primary, Social, Promotions, Updates, and Forums.

Outlook-Specific Folders

Archive: Where archived emails are stored.
Junk Email: Similar to Spam, where suspected junk emails are stored.
Clutter: A folder where less important emails are moved based on user behavior (Note: This feature is being phased out in favor of Focused Inbox).
Focused/Other: Part of the Focused Inbox feature, where important emails are separated from less important ones.

Summary
Common: Inbox, Sent, Drafts, Spam, Trash/Deleted.
Gmail-Specific: All Mail, Important, Starred, Chats, Categories.
Outlook-Specific: Archive, Junk Email, Clutter, Focused/Other.
 */