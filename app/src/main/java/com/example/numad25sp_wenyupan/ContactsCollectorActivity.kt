package com.example.numad25sp_wenyupan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.numad25sp_wenyupan.ui.theme.NUMAD25Sp_WenyuPanTheme
import kotlinx.coroutines.launch

data class Contact(val name: String, val phone: String)

class ContactsCollectorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NUMAD25Sp_WenyuPanTheme {
                ContactsScreen()
            }
        }
    }
}

@Composable
fun ContactsScreen() {
    var contacts by remember { mutableStateOf(listOf<Contact>()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<Contact?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingContact = null
                showDialog = true
            }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Contacts Collector", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (contacts.isEmpty()) {
                Text("No contacts added yet.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(contacts) { contact ->
                        ContactItem(
                            contact = contact,
                            onCall = {
                                if (isValidPhoneNumber(contact.phone)) {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                                    context.startActivity(dialIntent)
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Invalid phone number. Cannot start a call.")
                                    }
                                }
                            },
                            onEdit = {
                                editingContact = contact
                                showDialog = true
                            },
                            onDelete = {
                                contacts = contacts - contact
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Contact deleted",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        contacts = contacts + contact // Restore if Undo is clicked
                                    }
                                }
                            }
                        )
                    }
                }
            }

            if (showDialog) {
                AddOrEditContactDialog(
                    initialContact = editingContact,
                    onDismiss = { showDialog = false },
                    onSave = { newContact ->
                        when {
                            newContact.name.isEmpty() || newContact.phone.isEmpty() -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Please enter both name and phone number!", "OK")
                                }
                            }
                            !isValidPhoneNumber(newContact.phone) -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Invalid phone number. Please enter a valid one!", "OK")
                                }
                            }
                            else -> {
                                contacts = if (editingContact == null) {
                                    contacts + newContact
                                } else {
                                    contacts.map { if (it == editingContact) newContact else it }
                                }
                                showDialog = false

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Contact added!",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        contacts = contacts - newContact // Undo Contact Addition
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact, onCall: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onCall), // Clicking the contact box triggers the dialer if the number is valid
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Row for Name and Modify Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Name: ${contact.name}", style = MaterialTheme.typography.bodyLarge)
                TextButton(onClick = onEdit) {
                    Text("Modify", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Row for Phone and Delete Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Phone: ${contact.phone}", style = MaterialTheme.typography.bodyMedium)
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

// Function to validate phone numbers
fun isValidPhoneNumber(phone: String): Boolean {
    return phone.matches(Regex("^\\d{10,15}\$")) // Ensures only digits and at least 10 characters
}

@Composable
fun AddOrEditContactDialog(
    initialContact: Contact?,
    onDismiss: () -> Unit,
    onSave: (Contact) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(initialContact?.name ?: "")) }
    var phone by remember { mutableStateOf(TextFieldValue(initialContact?.phone ?: "")) }
    val snackbarHostState = remember { SnackbarHostState() } // Ensures Snackbar appears
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialContact == null) "Add Contact" else "Edit Contact") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Enter Name") }
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Enter Phone Number") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when {
                    name.text.isEmpty() || phone.text.isEmpty() -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please enter both name and phone number!", "OK")
                        }
                    }
                    !isValidPhoneNumber(phone.text) -> {
                        scope.launch {
                            snackbarHostState.showSnackbar("Invalid phone number. Please enter a valid one!", "OK")
                        }
                    }
                    else -> {
                        onSave(Contact(name.text, phone.text))
                        onDismiss() // Close the dialog only after successful validation
                    }
                }
            }) {
                Text(if (initialContact == null) "Add" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    // Display the Snackbar at the bottom
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.currentSnackbarData?.dismiss()
    }
    SnackbarHost(hostState = snackbarHostState)
}


@Preview(showBackground = true)
@Composable
fun ContactsPreview() {
    NUMAD25Sp_WenyuPanTheme {
        ContactsScreen()
    }
}
