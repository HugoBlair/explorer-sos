# ExplorerSOS

**A safety and trip-planning companion for solo adventurers.**

## Core Concept

ExplorerSOS is designed to provide peace of mind for outdoor enthusiasts and their loved ones. The core principle is "plan your trip, share your plan." By logging your adventure's details, the app ensures that if you're overdue, your emergency contacts can be automatically notified with all the critical information they need to help. It's a digital safety net for hiking, climbing, kayaking, and any adventure that takes you off the beaten path.

---

## Current Features

The current version of ExplorerSOS focuses on robust trip management, laying the foundation for the safety alert system.

*   **Trip Management:** A clean, intuitive interface to create, view, and edit all your trips.
*   **Trip Lifecycle:** Trips are organized into three clear states:
    *   **Ongoing:** The currently active trip is displayed prominently in its own section for immediate focus and action.
    *   **Upcoming:** Planned trips that have not yet started.
    *   **Past:** A history of all completed trips for your records.
*   **Active Trip UI:** The ongoing trip is presented in a larger, more detailed card, featuring an "End Trip" button for quick completion upon your safe return.
*   **Detailed Trip Planning:** Capture the essential details for any trip, including:
    *   A descriptive title (e.g., "Pinnacle Ridge Run").
    *   Start and end locations, powered by Google Places autocomplete for accuracy.
    *   Support for both one-way and round-trip journeys.
    *   Precise start and expected end date/times.
    *   Optional notes and descriptions.
*   **Offline First Architecture:** Using a local database, the app is fully functional without an internet connection. You can create plans and manage trips from anywhere. An internet connection is only needed for location searches.
*   **Modern Tech Stack:** Built with Kotlin, Jetpack Compose for the UI, and an MVVM architecture using Coroutines, Koin, and Room for a reliable and maintainable codebase.

---

## Future Vision & Roadmap

This is where ExplorerSOS will evolve from a trip logger into an indispensable safety tool. The following features are planned for future development.

### Tier 1: The Safety Alert System (The "SOS")

*   **Automated Overdue Alerts:** This is the cornerstone feature. If a trip is not manually marked as "Ended" by its `expectedEndDateTime`, the system will automatically trigger alerts.
*   **Emergency Contacts:** A dedicated section to add and manage a list of trusted contacts (Name, Phone, Email) who will receive alerts.
*   **Customizable Alert Messages:** You can pre-define the alert message. The message will automatically include the full details of your trip plan (route, start/end times, description).
*   **Multi-Channel Notifications:** Alerts will be sent via both SMS and Email to maximize the chance of being seen.
*   **Manual SOS Trigger:** A persistent, easily accessible button in the app to immediately send an emergency alert to all contacts, regardless of the trip status.

### Tier 2: Live Tracking & Intelligence

*   **GPS Location Tracking:** During an active trip, the app will periodically record your GPS coordinates.
    *   **Battery-Aware Tracking:** The tracking interval will be intelligent, balancing accuracy with battery conservation.
*   **Shareable Live-Tracking Map:** Generate a secure, time-limited web link for each trip that you can share with your contacts. This link will display your route and last known location on a map.
*   **"Check-In" System:** For longer trips, you can set optional "check-in" intervals. Missing a check-in could send a preliminary warning or notify you before escalating to a full alert.

### Tier 3: Enhanced Planning & Utility

*   **Gear & Checklists:**
    *   Create and save custom packing checklists for different activities (e.g., Day Hike, Ski Touring).
    *   Attach a checklist to a trip to ensure you've packed everything.
*   **Route & Weather Integration:**
    *   Import GPX files or draw your route directly on a map within the app.
    *   Integrate with a weather API to pull in forecasts for your trip's location and duration.
*   **Personal Profile:**
    *   A section for vital personal information (blood type, allergies, medical conditions) that can be optionally included in an emergency alert.
    *   View personal stats like total trips completed, distance traveled, etc.

Any and all of the future features could change at any time. They are just ideas.
