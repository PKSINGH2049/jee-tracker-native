package com.jeetracker.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeetracker.app.data.models.*
import com.jeetracker.app.ui.viewmodel.JeeTrackerViewModel

@Composable
fun DashboardScreen(viewModel: JeeTrackerViewModel) {
    val progressStats by viewModel.progressStats.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Progress Tracker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Overall Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Overall Progress",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Progress percentage
                Text(
                    text = "${progressStats?.percentage ?: 0}%",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066CC),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Progress bar
                LinearProgressIndicator(
                    progress = (progressStats?.percentage ?: 0) / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF0066CC),
                    trackColor = Color(0xFFE0E0E0)
                )

                // Stats
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        label = "Completed",
                        value = "${progressStats?.completedTopics ?: 0}",
                        icon = "✓"
                    )
                    StatItem(
                        label = "Total",
                        value = "${progressStats?.totalTopics ?: 0}",
                        icon = "📚"
                    )
                }
            }
        }

        // Subject breakdown
        Text(
            text = "By Subject",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(progressStats?.bySubject ?: emptyList()) { subject ->
                SubjectProgressItem(subject)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = icon, fontSize = 24.sp)
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SubjectProgressItem(subject: SubjectStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subject.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${(subject.completed * 100 / subject.total)}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066CC)
                )
            }

            LinearProgressIndicator(
                progress = subject.completed.toFloat() / subject.total,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFF0066CC),
                trackColor = Color(0xFFE0E0E0)
            )

            Text(
                text = "${subject.completed}/${subject.total} completed",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TopicsScreen(
    viewModel: JeeTrackerViewModel,
    subjectId: Int
) {
    val topics by viewModel.topics.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(topics) { topic ->
            TopicItem(
                topic = topic,
                onToggle = { viewModel.toggleTopic(topic.id) }
            )
        }
    }
}

@Composable
fun TopicItem(
    topic: Topic,
    onToggle: () -> Unit
) {
    var isCompleted by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                isCompleted = !isCompleted
                onToggle()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) Color(0xFFE8F5E9) else Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = topic.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                topic.description?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Checkbox(
                checked = isCompleted,
                onCheckedChange = { 
                    isCompleted = it
                    onToggle()
                }
            )
        }
    }
}

@Composable
fun AnalyticsScreen(viewModel: JeeTrackerViewModel) {
    val progressStats by viewModel.progressStats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Analytics",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Charts and detailed analytics would go here
        Text(
            text = "Detailed analytics for premium users",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun PricingScreen(
    onSubscribe: (String) -> Unit
) {
    val plans = listOf(
        Triple("Trial", "₹5", "2 days"),
        Triple("Monthly", "₹20", "per month"),
        Triple("Yearly", "₹100", "per year")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Upgrade Your Plan",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        items(plans) { (name, price, duration) ->
            PricingCard(
                name = name,
                price = price,
                duration = duration,
                onSubscribe = { onSubscribe(name.lowercase()) }
            )
        }
    }
}

@Composable
fun PricingCard(
    name: String,
    price: String,
    duration: String,
    onSubscribe: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = price,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066CC),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = duration,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onSubscribe,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0066CC))
            ) {
                Text("Subscribe", color = Color.White)
            }
        }
    }
}
