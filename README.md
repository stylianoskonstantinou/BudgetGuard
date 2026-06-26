# BudgetGuard

BudgetGuard is an Android application developed in Java using Android Studio. It helps users manage their personal finances by tracking income and expenses, setting monthly budgets, monitoring spending by category, and generating financial reports. The application stores all data locally using SQLite.

## Features

- Add and delete income and expense transactions.
- Set and manage a monthly budget.
- Create spending limits for different expense categories.
- View budget progress using graphical progress bars.
- Display monthly financial reports.
- Track current and previous month statistics.
- Store all data locally using SQLite.
- Simple and user-friendly interface built with Android Studio.

## Technologies

- Java
- Android Studio
- SQLite

## Installation

Clone the repository:

```bash
git clone https://github.com/stylianoskonstantinou/BudgetGuard.git
cd BudgetGuard
```

Open the project with **Android Studio**.

Allow Gradle to sync automatically.

Run the application on:

- Android Emulator
- Physical Android Device (API 24 or newer)

## Project Structure

- `app/` – Contains the application's source code, resources, and Android manifest.
- `app/src/main/java/` – Java source files for the application.
- `app/src/main/res/` – XML layouts, icons, strings, colors, and other resources.
- `DatabaseHelper.java` – Handles SQLite database creation and data management.
- `MainActivity.java` – Main dashboard of the application.
- `AddTransactionActivity.java` – Allows users to add income and expense transactions.
- `TransactionsActivity.java` – Displays all recorded transactions.
- `BudgetActivity.java` – Manages the monthly budget and spending limits.
- `ReportsActivity.java` – Generates monthly financial reports and statistics.
- `TransactionAdapter.java` – RecyclerView adapter used to display transactions.

## Future Improvements

- Cloud synchronization
- User authentication
- Export reports to PDF
- Dark mode

## Author

**Stylianos Konstantinou**

M.Sc. in Applied Informatics  
University of Thessaly