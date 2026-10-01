import sys

content = open('app/src/main/java/com/samay/app/ui/crisis/CrisisScreen.kt', 'r', encoding='utf-8').read()

original = """        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${line.number}")
                }
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SamayCrisis),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Llamar ahora", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }"""

replacement = """        com.samay.app.ui.theme.CrisisButton(
            text = "Llamar ahora",
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${line.number}")
                }
                context.startActivity(intent)
            }
        )"""

content = content.replace(original, replacement)
open('app/src/main/java/com/samay/app/ui/crisis/CrisisScreen.kt', 'w', encoding='utf-8').write(content)
