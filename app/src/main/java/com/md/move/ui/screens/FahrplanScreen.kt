package com.md.move.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

// Extensive list of Magdeburg stations
val STATIONS = listOf(
    "Agnetenstraße", "Albert-Vater-Straße", "Allee-Center", "Alte Neustadt", "Alter Markt",
    "Alt Olvenstedt", "Ambrosiusplatz", "Am Stern", "Am Fugger", "Arndtstraße",
    "Askanischer Platz", "August-Bebel-Damm", "Ballenstedter Straße", "Barberiniweg",
    "Barleber See", "Bebertaler Straße", "Beimsplatz", "Berliner Chaussee", "Birkenweiler",
    "Bodestraße", "Braunschweiger Straße", "Brenneckestraße", "Buckau (Wasserwerk)",
    "Buckauer Tor", "Bördepark", "Burgstaller Weg", "City Carré", "Cracau (Pezelstraße)",
    "Damaschkeplatz", "Damaschkestraße", "Diesdorf", "Domplatz", "Ebendorfer Chaussee",
    "Eisvogelstraße", "Eiskellerplatz", "Elbauenpark", "Enercon", "Enckestraße",
    "Erich-Weinert-Straße", "Euroaring", "Fachhochschule", "Felseneck", "Flora-Park",
    "Flechtinger Straße", "Freibad Süd", "Friedensweiler", "Friedrich-List-Straße",
    "Funkhaus", "Gartenstadt Pfahlberg", "Gerhart-Hauptmann-Straße", "Goezestraße",
    "Grenzweg", "Großer Silberberg", "Halberstädter Straße", "Hans-Grade-Straße",
    "Hannoverstraße", "Harsdorfer Straße", "Hasselbachplatz", "Hauptbahnhof",
    "Heidecker Weg", "Herrenkrug", "Heyrothsberge", "Hordorfer Straße", "Hundertwasserhaus",
    "Inselstraße", "Jacobstraße", "Jordanstraße", "Kanalbrücke", "Kannenstieg",
    "Kastanienstraße", "Kennedy-Platz", "Klinikum Olvenstedt", "Klusweg", "Kochstraße",
    "Kosmos-Promenade", "Kristallpalast", "Kroatenweg", "Kurfürstenstraße", "Kümmelsberg",
    "Leipziger Chaussee", "Leipziger Straße", "Leiterstraße", "Lemsdorf", "Lerchenwuhne",
    "Lessingstraße", "Listemannstraße", "Lorenzweg", "Lübecker Straße", "Luisenthaler Straße",
    "Magdeburg-Buckau", "Magdeburg-Neustadt", "Magdeburg-Sudenburg", "Marienbornstraße",
    "Maxim-Gorki-Straße", "Messegelände", "Milchweg", "Mittagstraße", "Möllendorffstraße",
    "Nachtweide", "Neustädter Platz", "Neustädter See", "Nicolaistraße", "Nordbrückenzug",
    "Nordpark", "Olvenstedt", "Olvenstedter Platz", "Opernhaus", "Otto-Richter-Straße",
    "Otto-von-Guericke-Straße", "Pappelallee", "Pechauer Platz", "Pfeiffersche Stiftungen",
    "Puppentheater", "Quarkberg", "Raiffeisenstraße", "Reform", "Reiterstraße",
    "Robert-Koch-Straße", "Rogätzer Straße", "Rothensee", "S-Bahnhof Neustadt",
    "S-Bahnhof Sudenburg", "S-Bahnhof Buckau", "Salbker Platz", "Schilfbreite",
    "Schleiermacherstraße", "Siedlung Reform", "Spielhagenstraße", "Sternstraße",
    "Sudenburg", "Südring", "Thiemstraße", "Turmschanzenstraße", "Universität",
    "Universitätsbibliothek", "Universitätsplatz", "Völpker Straße", "Warschauer Straße",
    "Wasserwerk", "Werder", "Westerhüsen", "Westring", "Wiener Straße", "Wissenschaftshafen",
    "Wolliner Straße", "ZOB", "Zollhaus", "Zoo"
).sorted()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FahrplanScreen() {
    var start by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var timeString by remember {
        mutableStateOf(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
    }
    var showResults by remember { mutableStateOf(false) }
    var recentSearches by remember { 
        mutableStateOf(listOf("Hasselbachplatz", "Hauptbahnhof", "Olvenstedt", "Eisvogelstraße")) 
    }

    var activeSearchField by remember { mutableIntStateOf(0) } // 0: none, 1: start, 2: destination
    var showTimePicker by remember { mutableStateOf(false) }

    val suggestions = remember(start, destination, activeSearchField) {
        val query = if (activeSearchField == 1) start else destination
        if (query.isNotEmpty()) {
            STATIONS.filter { it.contains(query, ignoreCase = true) }.take(5)
        } else emptyList()
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = timeString.split(":")[0].toIntOrNull() ?: 12,
            initialMinute = timeString.split(":")[1].toIntOrNull() ?: 0
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    timeString = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                    showResults = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Abbrechen") }
            },
            text = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    TimePicker(state = timePickerState)
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Train, 
                    null, 
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp).padding(end = 8.dp)
                )
                Text(
                    text = "MoveMD",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                tonalElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            SearchInputField(
                                value = start,
                                onValueChange = {
                                    start = it
                                    activeSearchField = 1
                                    showResults = false
                                },
                                label = "Startpunkt",
                                icon = Icons.Default.RadioButtonChecked,
                                iconTint = MaterialTheme.colorScheme.primary
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(start = 48.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            SearchInputField(
                                value = destination,
                                onValueChange = {
                                    destination = it
                                    activeSearchField = 2
                                    showResults = false
                                },
                                label = "Ziel wählen",
                                icon = Icons.Default.Place,
                                iconTint = MaterialTheme.colorScheme.error
                            )
                        }

                        FilledIconButton(
                            onClick = {
                                val temp = start
                                start = destination
                                destination = temp
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .size(40.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.SwapVert, "Vertauschen", modifier = Modifier.size(20.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            onClick = { showTimePicker = true },
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            border = border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = timeString, 
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (start.isNotBlank() && destination.isNotBlank()) {
                                    showResults = true
                                    activeSearchField = 0
                                    if (!recentSearches.contains(destination)) {
                                        recentSearches = (listOf(destination) + recentSearches).take(5)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(Icons.Default.Search, null, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Suchen", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (suggestions.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column {
                        suggestions.forEach { suggestion ->
                            ListItem(
                                headlineContent = { Text(suggestion) },
                                leadingContent = { Icon(Icons.Default.History, null) },
                                modifier = Modifier.clickable {
                                    if (activeSearchField == 1) start = suggestion
                                    else destination = suggestion
                                    activeSearchField = 0
                                }
                            )
                        }
                    }
                }
            }
        }

        if (!showResults && activeSearchField == 0) {
            item {
                Text(
                    text = "Zuletzt gesucht",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recentSearches) { search ->
                        Surface(
                            onClick = { 
                                destination = search
                                showResults = true 
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = border(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.History, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(6.dp))
                                Text(text = search, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }

        if (showResults && activeSearchField == 0) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Verbindungen ab $timeString",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = { showResults = false }) {
                        Icon(Icons.Default.Close, null)
                    }
                }
            }

            val results = generateMockResults(start, destination, timeString)
            items(results) { connection ->
                ModernConnectionCard(connection)
            }
        }
    }
}

@Composable
fun border(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    iconTint: Color
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        leadingIcon = { Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp)) },
        singleLine = true
    )
}

data class ConnectionStep(
    val line: String,
    val type: TransportType,
    val from: String,
    val to: String,
    val depTime: String,
    val arrTime: String,
    val delay: Int = 0 // Delay in minutes
)

enum class TransportType { TRAM, BUS, WALK }

data class ConnectionData(
    val steps: List<ConnectionStep>,
    val totalDuration: String,
    val price: String = "2,50 €"
)

@Composable
fun ModernConnectionCard(connection: ConnectionData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = connection.steps.first().depTime,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            null,
                            modifier = Modifier.padding(horizontal = 8.dp).size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = connection.steps.last().arrTime,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(
                        text = connection.totalDuration,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = connection.price,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            connection.steps.forEachIndexed { index, step ->
                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(40.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(getStepColor(step.type))
                                .border(2.dp, Color.White, CircleShape)
                        )
                        if (index < connection.steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .fillMaxHeight()
                                    .background(getStepColor(step.type).copy(alpha = 0.5f))
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(bottom = 20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = getStepColor(step.type),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Icon(getStepIcon(step.type), null, modifier = Modifier.size(14.dp), tint = Color.White)
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = step.line,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            // Time with potential delay
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = step.depTime,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (step.delay > 0) Color.Red else if (step.delay < 0) Color(0xFF4CAF50) else Color.Unspecified
                                    )
                                    if (step.delay != 0) {
                                        Text(
                                            text = if (step.delay > 0) " +${step.delay}" else " ${step.delay}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (step.delay > 0) Color.Red else Color(0xFF4CAF50),
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step.from,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Text(
                            text = "bis ${step.to}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                        )

                        if (index < connection.steps.size - 1) {
                            val nextStep = connection.steps[index + 1]
                            val waitTime = calculateWaitTime(step.arrTime, nextStep.depTime)
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(
                                    text = "Umsteigen: $waitTime Min Aufenthalt",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getStepColor(type: TransportType): Color = when (type) {
    TransportType.TRAM -> Color(0xFFD32F2F) // MVB Red
    TransportType.BUS -> Color(0xFF1976D2)  // Blue
    TransportType.WALK -> Color(0xFF757575) // Gray
}

fun getStepIcon(type: TransportType): ImageVector = when (type) {
    TransportType.TRAM -> Icons.Default.Train
    TransportType.BUS -> Icons.Default.DirectionsBus
    TransportType.WALK -> Icons.AutoMirrored.Filled.DirectionsWalk
}

fun calculateWaitTime(arr: String, dep: String): Int {
    return try {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateArr = format.parse(arr)
        val dateDep = format.parse(dep)
        if (dateArr != null && dateDep != null) {
            var diff = ((dateDep.time - dateArr.time) / (60 * 1000)).toInt()
            if (diff < 0) diff += 1440
            diff
        } else 5
    } catch (e: Exception) {
        5
    }
}

fun generateMockResults(start: String, dest: String, baseTime: String): List<ConnectionData> {
    val s = start.ifBlank { "Dein Standort" }
    val d = dest.ifBlank { "Ziel" }
    
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    val cal = Calendar.getInstance()
    try {
        val date = format.parse(baseTime)
        if (date != null) cal.time = date
    } catch (e: Exception) {}

    fun getTimeAfter(min: Int): String {
        cal.add(Calendar.MINUTE, min)
        return format.format(cal.time)
    }

    // Result 1: Quick connection
    val r1Dep1 = getTimeAfter(2)
    val r1Arr1 = getTimeAfter(8)
    val r1Dep2 = getTimeAfter(5)
    val r1Arr2 = getTimeAfter(11)

    // Reset calendar for Result 2
    try {
        val date = format.parse(baseTime)
        if (date != null) cal.time = date
    } catch (e: Exception) {}

    val r2Dep1 = getTimeAfter(8)
    val r2Arr1 = getTimeAfter(13)
    val r2Dep2 = getTimeAfter(7)
    val r2Arr2 = getTimeAfter(10)
    val r2Dep3 = getTimeAfter(5)
    val r2Arr3 = getTimeAfter(3)

    return listOf(
        ConnectionData(
            totalDuration = "24 Min",
            steps = listOf(
                ConnectionStep("Linie 4", TransportType.TRAM, s, "Alter Markt", r1Dep1, r1Arr1, delay = 2),
                ConnectionStep("Linie 1", TransportType.TRAM, "Alter Markt", d, r1Dep2, r1Arr2, delay = -1)
            )
        ),
        ConnectionData(
            totalDuration = "38 Min",
            steps = listOf(
                ConnectionStep("Linie 6", TransportType.TRAM, s, "Hauptbahnhof", r2Dep1, r2Arr1, delay = 5),
                ConnectionStep("Linie 10", TransportType.TRAM, "Hauptbahnhof", "Kastanienstraße", r2Dep2, r2Arr2),
                ConnectionStep("Bus 52", TransportType.BUS, "Kastanienstraße", d, r2Dep3, r2Arr3, delay = 3)
            )
        )
    )
}
