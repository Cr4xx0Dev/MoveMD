package com.md.move.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val details: String,
    val date: String
)

@Composable
fun NewsScreen() {
    val newsItems = remember {
        listOf(
            NewsItem(
                1,
                "Bauarbeiten am Hasselbachplatz",
                "Ab Montag kommt es zu Gleisbauarbeiten im Bereich des Hasselbachplatzes.",
                "Wegen dringender Sanierungsarbeiten an den Weichen wird der Hasselbachplatz für die Linien 2, 5, 9 und 10 gesperrt. Eine großräumige Umleitung über den Domplatz und die Otto-von-Guericke-Straße ist eingerichtet. Bitte planen Sie ca. 10 Minuten mehr Fahrzeit ein.",
                "Heute, 08:30"
            ),
            NewsItem(
                2,
                "Zusätzliche Bahnen zum FCM-Heimspiel",
                "Die MVB setzt am Samstag Sonderbahnen für die Anreise zur MDCC-Arena ein.",
                "Zum Heimspiel des 1. FC Magdeburg werden ab 11 Uhr zusätzliche Bahnen der Linie 6 zwischen Hauptbahnhof und Herrenkrug eingesetzt. Nach Spielende stehen ausreichend Kapazitäten für die Rückreise bereit.",
                "Gestern, 14:20"
            ),
            NewsItem(
                3,
                "Neue Elektrobusse im Testbetrieb",
                "Die Linie 52 wird ab sofort teilweise mit neuen E-Bussen bedient.",
                "Im Rahmen der Modernisierung unserer Flotte testen wir zwei neue Elektrobusse des Herstellers Solaris. Diese sind besonders leise und emissionsfrei. Wir freuen uns auf Ihr Feedback während der Fahrt!",
                "12. Okt, 11:05"
            ),
            NewsItem(
                4,
                "Tarifanpassung im marego-Verbund",
                "Informationen zu den neuen Ticketpreisen ab dem nächsten Monat.",
                "Ab dem 1. des nächsten Monats passen wir die Preise im gesamten marego-Verbund leicht an. Die Einzelfahrkarte im Stadttarif Magdeburg kostet dann 2,70 €. Monatstickets und Abos bleiben in vielen Preisstufen stabil.",
                "10. Okt, 09:15"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "MVB Aktuell",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(newsItems) { news ->
                ExpandedNewsCard(news)
            }
        }
    }
}

@Composable
fun ExpandedNewsCard(news: NewsItem) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = news.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = news.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = news.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    Text(
                        text = news.details,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
            
            TextButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (isExpanded) "Weniger anzeigen" else "Mehr erfahren")
            }
        }
    }
}
