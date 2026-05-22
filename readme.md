Markdown

# 🎭 NAMMA-MELA APP - Complete Development SOP

## Standard Operating Procedure for Development Team

---

## 📋 TABLE OF CONTENTS

1. [Project Overview](#project-overview)
2. [Screen-by-Screen Specifications](#screen-by-screen-specifications)
3. [Screen Flow & Navigation Map](#screen-flow--navigation-map)
4. [Backend Architecture & Database Schema](#backend-architecture--database-schema)
5. [API Requirements & Endpoints](#api-requirements--endpoints)
6. [Design Guidelines & UI/UX Specifications](#design-guidelines--uiux-specifications)
7. [Features & Functionality Breakdown](#features--functionality-breakdown)
8. [Technical Stack & Dependencies](#technical-stack--dependencies)
9. [Development Phases & Milestones](#development-phases--milestones)
10. [Testing Requirements](#testing-requirements)
11. [Additional Requirements](#additional-requirements)
12. [Final Checklist](#final-checklist)

---

## 🎯 PROJECT OVERVIEW

### Project Details

- **Project Name:** Namma-Mela (Our Drama)
- **Platform:** Android (Kotlin)
- **Purpose:** Digital booking platform for traditional Indian drama shows
- **Target Users:** Rural/Semi-urban drama enthusiasts & Drama company managers
- **App Type:** Dual interface (Customer + Manager)

### Core Problem Statement

Traditional drama companies in rural India lack digital presence for ticket booking. Customers cannot view show details, cast information, or book seats in advance.

### Solution

A bilingual (English/Kannada) mobile application that:
- Allows customers to browse tonight's shows
- View complete show details (cast, timing, pricing, venue)
- Book seats with visual seat selection
- Rate and review shows
- Enables managers to add/edit/delete shows and manage content

### User Types

#### Customer User
- **Authentication:** Phone number + OTP
- **Can:**
  - Browse shows
  - Book tickets
  - Rate/review shows
  - View booking history
- **Profile:** Persists across sessions

#### Manager User
- **Authentication:** Email-based login
- **Can:**
  - Add new shows
  - Edit/delete existing shows
  - Manage cast information
  - View booking statistics
  - Upload posters

---

## 📱 SCREEN-BY-SCREEN SPECIFICATIONS

### SCREEN 1: Language Selection + Role Selection

#### Purpose
First interaction screen where user selects language preference and their role (Customer/Manager)

#### UI Elements

**Top Section:**
- Language toggle button (Globe icon 🌐)
  - Position: Top-right corner
  - Shows "English | ಕನ್ನಡ"
  - Default: English
  - Tap to toggle between languages

**Center Section:**
- App Logo: Drama mask icon (🎭)
- App Name: "NAMMA-MELA" (large, bold)
- Tagline: "Bringing Drama to Life" / "ನಾಟಕವನ್ನು ಜೀವಂತಗೊಳಿಸುವುದು"

**Bottom Section:**
Two large buttons (full width, stacked):
1. **"I'm a Customer"** button
   - Icon: User icon
   - Action: Navigate to Phone Login
2. **"I'm a Manager"** button
   - Icon: Settings/Admin icon
   - Action: Navigate to Email Login

#### Functionality
- Language selection stores preference in SharedPreferences
- All subsequent screens load in selected language
- Role selection determines authentication method

#### Backend Requirements
- Store language preference locally
- No API call needed for this screen

#### Visual Mockup
┌─────────────────────────────┐
│ 🌐 En|ಕನ್ │
│ │
│ 🎭 │
│ │
│ NAMMA-MELA │
│ ───────────────── │
│ │
│ "Bringing Drama to Life" │
│ │
│ │
│ ┌─────────────────────┐ │
│ │ 👤 I'm a Customer │ │
│ └─────────────────────┘ │
│ │
│ ┌─────────────────────┐ │
│ │ ⚙️ I'm a Manager │ │
│ └─────────────────────┘ │
│ │
└─────────────────────────────┘

text


---

### SCREEN 2A: Customer Login (Phone + OTP)

#### Purpose
Authenticate customer using phone number and OTP verification

#### UI Elements

**Screen 1 - Phone Number Entry:**
- Header: "Welcome to Namma-Mela"
- Input field: Phone number (10 digits)
  - Country code: +91 (fixed)
  - Placeholder: "Enter your mobile number"
  - Keyboard type: Numeric
- Button: "Send OTP"
- Back button to return to role selection

**Screen 2 - OTP Verification:**
- Header: "Enter OTP"
- Subtext: "OTP sent to +91 XXXXX XXXXX"
- Input: 6-digit OTP boxes
- Timer: "Resend OTP in 30s"
- Button: "Verify & Continue"
- Link: "Edit phone number"

#### Functionality
1. User enters phone number
2. App sends OTP via Firebase Authentication
3. User enters OTP
4. System verifies OTP
5. If verified:
   - Check if user exists in database
   - If new: Create user profile
   - If existing: Fetch user data
6. Navigate to Home Screen

#### Backend Requirements

**API Endpoint 1: Send OTP**
```http
POST /auth/send-otp
Content-Type: application/json

Request:
{
  "phone": "+919876543210"
}

Response:
{
  "success": true,
  "message": "OTP sent"
}
API Endpoint 2: Verify OTP

http

POST /auth/verify-otp
Content-Type: application/json

Request:
{
  "phone": "+919876543210",
  "otp": "123456"
}

Response:
{
  "success": true,
  "userId": "user_123",
  "token": "jwt_token_here",
  "isNewUser": false
}
Visual Mockup
text

┌─────────────────────────────┐
│ ← Back                      │
├─────────────────────────────┤
│                             │
│   Welcome to Namma-Mela     │
│                             │
│   Enter your mobile number  │
│                             │
│  ┌──────────────────────┐  │
│  │ +91 | [9876543210]   │  │
│  └──────────────────────┘  │
│                             │
│  ┌──────────────────────┐  │
│  │    SEND OTP          │  │
│  └──────────────────────┘  │
│                             │
└─────────────────────────────┘
SCREEN 2B: Manager Login (Email)
Purpose
Authenticate manager using email and password

UI Elements
Header: "Manager Login"
Input 1: Email address
Placeholder: "manager@dramacompany.com"
Keyboard type: Email
Input 2: Password
Placeholder: "Enter password"
Show/Hide password toggle
Button: "Login"
Link: "Forgot Password?"
Back button to role selection
Functionality
User enters email and password
System validates credentials
If valid: Navigate to Manager Dashboard
If invalid: Show error message
Backend Requirements
API Endpoint: Manager Login

http

POST /auth/manager-login
Content-Type: application/json

Request:
{
  "email": "manager@example.com",
  "password": "password123"
}

Response:
{
  "success": true,
  "managerId": "mgr_123",
  "token": "jwt_token_here"
}
Visual Mockup
text

┌─────────────────────────────┐
│ ← Back                      │
├─────────────────────────────┤
│                             │
│     Manager Login           │
│                             │
│  Email                      │
│  ┌──────────────────────┐  │
│  │ manager@example.com  │  │
│  └──────────────────────┘  │
│                             │
│  Password                   │
│  ┌──────────────────────┐  │
│  │ ••••••••••      👁️  │  │
│  └──────────────────────┘  │
│                             │
│  ┌──────────────────────┐  │
│  │       LOGIN          │  │
│  └──────────────────────┘  │
│                             │
│      Forgot Password?       │
│                             │
└─────────────────────────────┘
SCREEN 3: Home Screen (Customer - Tonight's Shows List)
Purpose
Display all shows scheduled for tonight with booking options

UI Elements
Top Bar:

Hamburger menu (left)
App name "NAMMA-MELA" (center)
Notification icon (right)
Profile icon (right)
Header Section:

Title: "🎭 Tonight's Shows"
Date indicator: "Today, [Date]"
Show Cards (Scrollable List):
Each card contains:

Poster image (160dp x 240dp)
Play name (e.g., "Ramayana Drama")
Time: "⏰ 7:00 PM | 3 hrs duration"
Venue: "📍 Village Square"
Seat availability: "🟢 45 seats available"
Green if >20
Yellow if 5-20
Red if <5
Price range: "₹20 - ₹100"
Rating: "⭐ 4.8 (125 reviews)"
"BOOK NOW" button (primary action)
Bottom Navigation:

Home 🏠 (active)
Plays 🎭
My Bookings 🎟️
Fan Wall 💬
Profile 👤
Functionality
Fetch all shows for today from API
Display in card format
Click on card: Navigate to Play Detail Screen
Click "Book Now": Navigate to Seat Selection
Pull to refresh
Backend Requirements
API Endpoint: Get Today's Shows

http

GET /shows/today
Authorization: Bearer {token}

Response:
{
  "shows": [
    {
      "id": "show_123",
      "name": "Ramayana Drama",
      "nameKannada": "ರಾಮಾಯಣ ನಾಟಕ",
      "description": "Epic tale of Lord Rama...",
      "posterUrl": "https://storage.../poster.jpg",
      "startTime": "19:00",
      "duration": 180,
      "venue": "Village Square",
      "venueKannada": "ಗ್ರಾಮ ಚೌಕ",
      "seatsAvailable": 45,
      "totalSeats": 80,
      "priceRange": {
        "min": 20,
        "max": 100
      },
      "rating": 4.8,
      "reviewCount": 125
    }
  ]
}
Visual Mockup
text

┌─────────────────────────────┐
│ ☰ NAMMA-MELA      🔔 👤   │
├─────────────────────────────┤
│                             │
│ 🎭 Tonight's Shows          │
│ Today, Jan 20, 2024         │
│                             │
│ ┌─────────────────────────┐ │
│ │ [POSTER IMAGE]          │ │
│ │                         │ │
│ │ Ramayana Drama          │ │
│ │ ⏰ 7:00 PM | 3 hrs      │ │
│ │ 📍 Village Square       │ │
│ │ 🟢 45 seats available   │ │
│ │ ₹20 - ₹100              │ │
│ │ ⭐ 4.8 (125 reviews)    │ │
│ │ [ BOOK NOW ]            │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ [POSTER IMAGE]          │ │
│ │ Mahabharata             │ │
│ │ ⏰ 9:00 PM | 4 hrs      │ │
│ │ ...                     │ │
│ └─────────────────────────┘ │
│                             │
├─────────────────────────────┤
│ 🏠  🎭  🎟️  💬  👤        │
└─────────────────────────────┘
SCREEN 4: Play Detail Screen
Purpose
Show complete information about a specific drama show

UI Elements
Header:

Back button
Play name
Favorite icon (heart)
Content (Scrollable):

Large Poster Image

Full-width banner image
Title Section

Play name (large, bold)
Star rating: ⭐⭐⭐⭐⭐ (4.8)
Quick Info Cards

Date: 📅 Today
Time: ⏰ 7:00 PM - 10:00 PM
Duration: ⌛ 3 hours
Venue: 📍 Village Square Stage
Pricing Section

Front Row: ₹100
Middle Row: ₹50
Back Row: ₹20
About Section

Description text (expandable)
"Read more" link if text is long
Cast Section

"View Full Cast →" button
Preview: First 3 cast member photos in circles
Reviews Section

Average rating with star breakdown
"See all reviews →" button
Preview: Top 2 recent reviews
Bottom Fixed Section:

Large "🎟️ BOOK SEATS" button (primary color)
Functionality
Fetch show details from API using show ID
Lazy load reviews
Click "View Cast": Navigate to Cast Screen
Click "Book Seats": Navigate to Seat Selection
Favorite icon: Add to favorites (save locally + API)
Backend Requirements
API Endpoint: Get Show Details

http

GET /shows/{showId}
Authorization: Bearer {token}

Response:
{
  "id": "show_123",
  "name": "Ramayana Drama",
  "nameKannada": "ರಾಮಾಯಣ ನಾಟಕ",
  "description": "Epic tale of Lord Rama performed by Sri Krishna Drama Company...",
  "posterUrl": "https://...",
  "showDate": "2024-01-20",
  "startTime": "19:00",
  "endTime": "22:00",
  "duration": 180,
  "venue": "Village Square Stage",
  "pricing": {
    "front": 100,
    "middle": 50,
    "back": 20
  },
  "rating": 4.8,
  "reviewCount": 125,
  "cast": [
    {
      "id": "cast_1",
      "name": "Ravi Kumar",
      "photoUrl": "https://...",
      "role": "HERO",
      "character": "Rama"
    }
  ]
}
Visual Mockup
text

┌─────────────────────────────┐
│ ← Back   Ramayana      ❤️  │
├─────────────────────────────┤
│ ┌─────────────────────────┐ │
│ │                         │ │
│ │  [LARGE POSTER IMAGE]   │ │
│ │                         │ │
│ └─────────────────────────┘ │
│                             │
│ 🎭 RAMAYANA DRAMA           │
│ ⭐⭐⭐⭐⭐ (4.8)            │
│                             │
│ 📅 Today                    │
│ ⏰ 7:00 PM - 10:00 PM       │
│ ⌛ Duration: 3 hours        │
│ 📍 Village Square Stage     │
│                             │
│ 💰 Pricing:                 │
│    Front Row: ₹100          │
│    Middle: ₹50              │
│    Back: ₹20                │
│                             │
│ 📖 About:                   │
│ Epic tale of Lord Rama      │
│ performed by Sri Krishna... │
│ [Read more]                 │
│                             │
│ 👥 [View Cast →]            │
│                             │
│ ⭐ Reviews (125)             │
│ [See all →]                 │
│                             │
├─────────────────────────────┤
│  ┌─────────────────────┐   │
│  │  🎟️ BOOK SEATS      │   │
│  └─────────────────────┘   │
└─────────────────────────────┘
SCREEN 5: Cast Display Screen
Purpose
Display all cast members categorized by role

UI Elements
Header:

Back button
Title: "Tonight's Cast"
Content (Scrollable):

Sections (categorized):

🌟 LEAD ARTISTS

Grid layout (2 columns)
Each card:
Circular photo (80dp)
Name
Role badge ("HERO", "HEROINE")
Character name in parentheses
🎤 SINGERS

Same grid layout
Role badge: "SINGER"
😂 COMEDIANS

Same grid layout
Role badge: "COMEDIAN"
🎭 SUPPORTING CAST

Same grid layout
Role badge: Character name
Functionality
Fetch cast data from API
Display in categorized sections
Optional: Click on cast member for bio (future enhancement)
Backend Requirements
API Endpoint: Get Cast

http

GET /shows/{showId}/cast
Authorization: Bearer {token}

Response:
{
  "cast": [
    {
      "id": "cast_1",
      "name": "Ravi Kumar",
      "nameKannada": "ರವಿ ಕುಮಾರ್",
      "role": "HERO",
      "character": "Rama",
      "photoUrl": "https://...",
      "category": "LEAD"
    },
    {
      "id": "cast_2",
      "name": "Sita Menon",
      "role": "HEROINE",
      "character": "Sita",
      "photoUrl": "https://...",
      "category": "LEAD"
    },
    {
      "id": "cast_3",
      "name": "Lakshmi V",
      "role": "SINGER",
      "photoUrl": "https://...",
      "category": "SINGER"
    }
  ]
}
Visual Mockup
text

┌─────────────────────────────┐
│ ← Back   Tonight's Cast     │
├─────────────────────────────┤
│                             │
│ 🌟 LEAD ARTISTS             │
│ ─────────────────────       │
│                             │
│ ┌──────────┐ ┌──────────┐  │
│ │ [PHOTO]  │ │ [PHOTO]  │  │
│ │          │ │          │  │
│ │ Ravi K.  │ │ Sita M.  │  │
│ │  HERO    │ │ HEROINE  │  │
│ │ (Rama)   │ │  (Sita)  │  │
│ └──────────┘ └──────────┘  │
│                             │
│ 🎤 SINGERS                  │
│ ─────────────────────       │
│                             │
│ ┌──────────┐ ┌──────────┐  │
│ │ [PHOTO]  │ │ [PHOTO]  │  │
│ │ Lakshmi  │ │ Suresh   │  │
│ │ SINGER   │ │ SINGER   │  │
│ └──────────┘ └──────────┘  │
│                             │
│ 😂 COMEDIANS                │
│ ─────────────────────       │
│                             │
│ ┌──────────┐ ┌──────────┐  │
│ │ [PHOTO]  │ │ [PHOTO]  │  │
│ │  Kumar   │ │   Raju   │  │
│ │ COMEDIAN │ │ COMEDIAN │  │
│ └──────────┘ └──────────┘  │
│                             │
└─────────────────────────────┘
SCREEN 6: Seat Selection Screen (MOST IMPORTANT)
Purpose
Visual interactive seat map for booking

UI Elements
Header:

Back button
Title: "Select Seats"
Selected count: "2 selected"
Stage Indicator:

Visual representation of stage at top
Label: "🎭 STAGE 🎭"
Seat Map:
Visual grid layout with:

Front Row (₹100)

Rows A-B
8 seats per row
Seat representation:
🟢 Available (green circle)
🔴 Booked (red circle)
🟡 Selected (yellow circle)
Click to select/deselect
Middle Row (₹50)

Rows C-E
8 seats per row
Same color coding
Back Row (₹20)

Rows F-H
8 seats per row
Same color coding
Legend:

🟢 Available
🔴 Booked
🟡 Your Selection
Bottom Summary:

Selected seats: "A7, B4"
Total price: "₹200"
"CONFIRM BOOKING" button
Functionality
Fetch seat availability from API
Render seat map with status
On tap:
If available: Select (change to yellow)
If selected: Deselect
If booked: Show toast "Already booked"
Calculate total price dynamically
Max 10 seats per booking
Click Confirm: Navigate to confirmation screen
Backend Requirements
API Endpoint 1: Get Seats

http

GET /shows/{showId}/seats
Authorization: Bearer {token}

Response:
{
  "seats": [
    {
      "id": "seat_A1",
      "row": "A",
      "column": 1,
      "category": "FRONT",
      "price": 100,
      "isBooked": false
    },
    {
      "id": "seat_A2",
      "row": "A",
      "column": 2,
      "category": "FRONT",
      "price": 100,
      "isBooked": true
    }
  ]
}
API Endpoint 2: Create Booking

http

POST /bookings/create
Authorization: Bearer {token}
Content-Type: application/json

Request:
{
  "showId": "show_123",
  "userId": "user_123",
  "seats": ["seat_A7", "seat_B4"],
  "totalPrice": 200
}

Response:
{
  "success": true,
  "bookingId": "booking_123",
  "qrCode": "data:image/png;base64,iVBOR..."
}
Visual Mockup
text

┌─────────────────────────────┐
│ ← Back   Select Seats       │
│                    2 selected│
├─────────────────────────────┤
│                             │
│ ┌─────────────────────────┐ │
│ │     🎭 STAGE 🎭         │ │
│ └─────────────────────────┘ │
│                             │
│   FRONT ROW (₹100)          │
│   1 2 3 4 5 6 7 8           │
│ A 🟢🟢🔴🔴🟢🟢🟡🟢          │
│ B 🟢🔴🔴🟡🟢🟢🟢🟢          │
│                             │
│   MIDDLE ROW (₹50)          │
│ C 🟢🟢🟢🔴🔴🟢🟢🟢          │
│ D 🟢🟢🟢🟢🟢🟢🟢🟢          │
│ E 🟢🟢🔴🟢🟢🟢🟢🟢          │
│                             │
│   BACK ROW (₹20)            │
│ F 🟢🟢🟢🟢🟢🟢🟢🟢          │
│ G 🟢🟢🟢🟢🟢🟢🟢🟢          │
│ H 🟢🟢🟢🟢🟢🟢🟢🟢          │
│                             │
│ ───────────────────────     │
│ 🟢 Available  🔴 Booked     │
│ 🟡 Selected                 │
│ ───────────────────────     │
│                             │
│ Selected: A7, B4            │
│ Total: ₹200                 │
│                             │
│ ┌─────────────────────────┐ │
│ │  ✓ CONFIRM BOOKING      │ │
│ └─────────────────────────┘ │
└─────────────────────────────┘
SCREEN 7: Booking Confirmation
Purpose
Show successful booking with ticket details

UI Elements
Success Animation:

Green checkmark ✓
"Booking Successful!" message
Ticket Card:
Visual ticket design with:

QR code (for entry verification)
Show name
Date & Time
Seat numbers
Venue
Total amount paid
Booking ID
Action Buttons:

📥 Download Ticket (save as image)
📤 Share Ticket (WhatsApp, etc.)
"⭐ Rate This Show" button
Navigation:

"BACK TO HOME" button
Functionality
Generate QR code with booking ID
Save booking to user's history
Send confirmation SMS/notification
Enable ticket download as PNG
Share functionality
Backend Requirements
API Endpoint: Get Booking

http

GET /bookings/{bookingId}
Authorization: Bearer {token}

Response:
{
  "id": "booking_123",
  "bookingId": "NM12345",
  "showName": "Ramayana Drama",
  "showDate": "2024-01-20",
  "showTime": "19:00",
  "venue": "Village Square",
  "seats": ["A7", "B4"],
  "totalPrice": 200,
  "qrCode": "data:image/png;base64...",
  "status": "CONFIRMED"
}
Visual Mockup
text

┌─────────────────────────────┐
│   Booking Confirmed         │
├─────────────────────────────┤
│                             │
│         ✓                   │
│    (Green Check)            │
│                             │
│  🎉 Booking Successful!     │
│                             │
│ ┌─────────────────────────┐ │
│ │   YOUR TICKET           │ │
│ │ ─────────────────────   │ │
│ │ Play: Ramayana          │ │
│ │ Date: Today             │ │
│ │ Time: 7:00 PM           │ │
│ │ Seat: A7, B4 (Front)    │ │
│ │ Amount: ₹200            │ │
│ │                         │ │
│ │     [QR CODE]           │ │
│ │                         │ │
│ │ Booking ID: NM12345     │ │
│ └─────────────────────────┘ │
│                             │
│ [📥 Download] [📤 Share]    │
│                             │
│ ┌─────────────────────────┐ │
│ │  ⭐ RATE THIS SHOW      │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │   BACK TO HOME          │ │
│ └─────────────────────────┘ │
└─────────────────────────────┘
SCREEN 8: Fan Wall (Reviews & Comments)
Purpose
Social feed for show discussions and reviews

UI Elements
Header:

Back button
Title: "Fan Wall 🎭"
Post Input:

Text area: "Share your experience..."
"POST →" button (only after typing)
Feed (Scrollable):
Each post shows:

User avatar & name
Time ago (e.g., "2 hrs ago")
Comment text
❤️ Like button with count
💬 Reply button
Filter Options:

Tabs: "All Shows" | "Ramayana" | "Mahabharata"
Functionality
Fetch comments from API
Post new comment
Like/unlike comments
Real-time updates (optional: using WebSocket)
Pagination: Load more on scroll
Backend Requirements
API Endpoint 1: Get Comments

http

GET /comments?showId={showId}&page=1&limit=20
Authorization: Bearer {token}

Response:
{
  "comments": [
    {
      "id": "comment_1",
      "userId": "user_123",
      "userName": "Ramesh K.",
      "userAvatar": "https://...",
      "message": "Amazing performance by Ravi!",
      "likes": 24,
      "createdAt": "2024-01-20T20:00:00Z",
      "isLikedByUser": false
    }
  ],
  "hasMore": true
}
API Endpoint 2: Post Comment

http

POST /comments/create
Authorization: Bearer {token}
Content-Type: application/json

Request:
{
  "showId": "show_123",
  "message": "Loved the show!"
}

Response:
{
  "success": true,
  "commentId": "comment_456"
}
API Endpoint 3: Like Comment

http

POST /comments/{commentId}/like
Authorization: Bearer {token}

Response:
{
  "success": true,
  "likes": 25
}
Visual Mockup
text

┌─────────────────────────────┐
│ ← Back   Fan Wall 🎭        │
├─────────────────────────────┤
│                             │
│ 💬 Share your experience    │
│ ┌─────────────────────────┐ │
│ │ Write a comment...      │ │
│ │                         │ │
│ │              [POST →]   │ │
│ └─────────────────────────┘ │
│                             │
│ ───────────────────────     │
│                             │
│ 👤 Ramesh K.  2 hrs ago     │
│ "Amazing performance by     │
│  Ravi as Rama! Loved it!"   │
│ ❤️ 24    💬 Reply           │
│                             │
│ ───────────────────────     │
│                             │
│ 👤 Priya S.   5 hrs ago     │
│ "The singers were superb.   │
│  Will come again!"          │
│ ❤️ 18    💬 Reply           │
│                             │
│ ───────────────────────     │
│                             │
│ 👤 Suresh M.  1 day ago     │
│ "Best Company Nataka! 🎭"   │
│ ❤️ 45    💬 Reply           │
│                             │
└─────────────────────────────┘
SCREEN 9: Manager Dashboard
Purpose
Central control panel for drama company managers

UI Elements
Header:

Hamburger menu
"Manager Panel"
Profile icon
Welcome Section:

"👋 Welcome, Manager!"
Stats Cards:

🎟️ Today's Bookings: 47/80
💰 Revenue: ₹3,250
🎭 Total Active Shows: 5
Quick Actions (Large Buttons):

➕ Add New Play
✏️ Edit Existing Play
👥 Manage Cast
📊 View Bookings
🔄 Reset Seats (for new day)
Functionality
Fetch manager dashboard stats
Navigate to respective screens on button click
Backend Requirements
API Endpoint: Get Dashboard Stats

http

GET /manager/dashboard
Authorization: Bearer {manager_token}

Response:
{
  "todayBookings": 47,
  "totalSeats": 80,
  "revenue": 3250,
  "activeShows": 5,
  "upcomingShows": [
    {
      "id": "show_123",
      "name": "Ramayana Drama",
      "showDate": "2024-01-20",
      "bookings": 47
    }
  ]
}
Visual Mockup
text

┌─────────────────────────────┐
│ ☰ Manager Panel        👤  │
├─────────────────────────────┤
│                             │
│ 👋 Welcome, Manager!        │
│                             │
│ 📊 TODAY'S STATS            │
│ ┌─────────────────────────┐ │
│ │ 🎟️ Bookings: 47/80      │ │
│ │ 💰 Revenue: ₹3,250      │ │
│ │ 🎭 Total Plays: 5       │ │
│ └─────────────────────────┘ │
│                             │
│ ⚡ QUICK ACTIONS            │
│                             │
│ ┌─────────────────────────┐ │
│ │  ➕ Add New Play        │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │  ✏️ Edit Existing Play  │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │  👥 Manage Cast         │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │  📊 View Bookings       │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │  🔄 Reset Seats         │ │
│ └─────────────────────────┘ │
└─────────────────────────────┘
SCREEN 10: Add/Edit Play (Manager)
Purpose
Form to create or modify show details

UI Elements (Form Fields)
Play Information:

Play Name * (required)

Text input
Description

Multi-line text area
Duration (hours)

Number picker (1-5 hours)
Show Time

Time picker
Default: 7:00 PM
Show Date

Date picker
Default: Today
Venue

Text input or dropdown of saved venues
Poster Upload:

Image picker
Preview uploaded image
"📸 Upload Poster" button
Pricing:

Front Row: ₹ [input]
Middle Row: ₹ [input]
Back Row: ₹ [input]
Cast Assignment:

"Add Cast Member" button
List of added cast with remove option
Actions:

"💾 SAVE PLAY" button (bottom)
"Cancel" link
Functionality
Image upload to cloud storage
Form validation
Save to database via API
On success: Navigate back to dashboard
Backend Requirements
API Endpoint 1: Create Show

http

POST /shows/create
Authorization: Bearer {manager_token}
Content-Type: application/json

Request:
{
  "name": "Ramayana Drama",
  "nameKannada": "ರಾಮಾಯಣ ನಾಟಕ",
  "description": "Epic tale...",
  "posterUrl": "https://...",
  "showDate": "2024-01-20",
  "startTime": "19:00",
  "duration": 180,
  "venue": "Village Square",
  "pricing": {
    "front": 100,
    "middle": 50,
    "back": 20
  },
  "cast": [
    {
      "name": "Ravi Kumar",
      "role": "HERO",
      "character": "Rama",
      "photoUrl": "https://..."
    }
  ]
}

Response:
{
  "success": true,
  "showId": "show_123"
}
API Endpoint 2: Update Show

http

PUT /shows/{showId}/update
Authorization: Bearer {manager_token}
Content-Type: application/json

Request:
{
  "name": "Updated Name",
  "pricing": {
    "front": 120,
    "middle": 60,
    "back": 25
  }
}

Response:
{
  "success": true
}
API Endpoint 3: Upload Image

http

POST /upload/image
Authorization: Bearer {token}
Content-Type: multipart/form-data

Request:
FormData with image file

Response:
{
  "success": true,
  "imageUrl": "https://storage.../image.jpg"
}
Visual Mockup
text

┌─────────────────────────────┐
│ ← Back   Add New Play       │
├─────────────────────────────┤
│                             │
│ 🎭 Play Information         │
│                             │
│ Play Name *                 │
│ ┌─────────────────────────┐ │
│ │ Enter play name         │ │
│ └─────────────────────────┘ │
│                             │
│ Description                 │
│ ┌─────────────────────────┐ │
│ │                         │ │
│ │                         │ │
│ └─────────────────────────┘ │
│                             │
│ Duration (hours)            │
│ ┌─────────────────────────┐ │
│ │ 3                       │ │
│ └─────────────────────────┘ │
│                             │
│ Show Time                   │
│ ┌─────────────────────────┐ │
│ │ 🕐 7:00 PM              │ │
│ └─────────────────────────┘ │
│                             │
│ 📸 Upload Poster            │
│ ┌─────────────────────────┐ │
│ │  [+ ADD IMAGE]          │ │
│ └─────────────────────────┘ │
│                             │
│ 💰 Pricing                  │
│ Front: [100] Mid: [50]      │
│ Back: [20]                  │
│                             │
│ ┌─────────────────────────┐ │
│ │   💾 SAVE PLAY          │ │
│ └─────────────────────────┘ │
└─────────────────────────────┘
🗺️ SCREEN FLOW & NAVIGATION MAP
App Flow Diagram
text

                START
                  ↓
         ┌─────────────────┐
         │  SPLASH SCREEN  │
         │      (2s)       │
         └────────┬────────┘
                  ↓
         ┌─────────────────┐
         │   LANGUAGE +    │
         │  ROLE SELECT    │
         └────┬──────┬─────┘
              ↓      ↓
        CUSTOMER  MANAGER
              ↓      ↓
         ┌────────┐ ┌────────┐
         │PHONE   │ │EMAIL   │
         │OTP     │ │LOGIN   │
         └───┬────┘ └───┬────┘
             ↓          ↓
         ┌────────┐ ┌──────────┐
         │ HOME   │ │ MANAGER  │
         │(Shows) │ │DASHBOARD │
         └─┬──────┘ └────┬─────┘
           ↓             ↓
    ┌──────────┐   ┌──────────┐
    │  PLAY    │   │ ADD/EDIT │
    │ DETAIL   │   │   PLAY   │
    └─┬────────┘   └──────────┘
      ↓
    ┌──────────┐
    │  CAST    │
    │  VIEW    │
    └─┬────────┘
      ↓
    ┌──────────┐
    │  SEAT    │
    │SELECTION │
    └─┬────────┘
      ↓
    ┌──────────┐
    │ BOOKING  │
    │CONFIRMED │
    └─┬────────┘
      ↓
    [HOME]
Bottom Navigation Accessible Screens
From any customer screen, user can access:

🏠 Home → Tonight's Shows
🎭 Plays → All shows (past/future)
🎟️ My Bookings → Booking history
💬 Fan Wall → Community feed
👤 Profile → User settings
🏗️ BACKEND ARCHITECTURE & DATABASE SCHEMA
Technology Stack
Backend Options:

Option 1: Node.js + MongoDB

Express.js (framework)
MongoDB + Mongoose (database)
JWT (authentication)
Multer (file upload)
Firebase Storage (images)
Twilio/Firebase (SMS/OTP)
Option 2: Firebase (Serverless)

Cloud Firestore (database)
Cloud Functions (API)
Firebase Auth (authentication)
Cloud Storage (images)
Cloud Messaging (notifications)
Recommended: Firebase for faster development and built-in features

Database Schema
Collection/Table: users
JSON

{
  "_id": "user_123",
  "phone": "+919876543210",
  "name": "Rajesh Kumar",
  "email": "rajesh@example.com",
  "role": "CUSTOMER",
  "favoriteShows": ["show_123", "show_456"],
  "languagePreference": "kannada",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-20T12:00:00Z"
}
Fields:

_id: Unique user identifier
phone: User's phone number (with country code)
name: User's display name
email: Optional email
role: "CUSTOMER" or "MANAGER"
favoriteShows: Array of show IDs
languagePreference: "english" or "kannada"
createdAt: Account creation timestamp
updatedAt: Last updated timestamp
Collection/Table: managers
JSON

{
  "_id": "mgr_123",
  "email": "manager@dramacompany.com",
  "passwordHash": "$2b$10$hashed_password_here",
  "companyName": "Sri Krishna Drama Company",
  "phone": "+919876543210",
  "createdAt": "2024-01-01T00:00:00Z"
}
Fields:

_id: Unique manager identifier
email: Manager's email (login credential)
passwordHash: Bcrypt hashed password
companyName: Name of drama company
phone: Contact number
createdAt: Account creation timestamp
Collection/Table: shows
JSON

{
  "_id": "show_123",
  "name": "Ramayana Drama",
  "nameKannada": "ರಾಮಾಯಣ ನಾಟಕ",
  "description": "Epic tale of Lord Rama performed by Sri Krishna Drama Company...",
  "descriptionKannada": "ಶ್ರೀಕೃಷ್ಣ ನಾಟಕ ಸಂಘದಿಂದ ಪ್ರದರ್ಶಿಸಲಾದ ಶ್ರೀರಾಮನ ಮಹಾಕಾವ್ಯ...",
  "posterUrl": "https://storage.googleapis.com/.../poster.jpg",
  "managerId": "mgr_123",
  "showDate": "2024-01-20",
  "startTime": "19:00",
  "endTime": "22:00",
  "duration": 180,
  "venue": "Village Square Stage",
  "venueKannada": "ಗ್ರಾಮ ಚೌಕ ವೇದಿಕೆ",
  "pricing": {
    "front": 100,
    "middle": 50,
    "back": 20
  },
  "totalSeats": 80,
  "seatsBooked": 35,
  "rating": 4.8,
  "reviewCount": 125,
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:00:00Z",
  "updatedAt": "2024-01-20T14:30:00Z"
}
Fields:

_id: Unique show identifier
name: Show name in English
nameKannada: Show name in Kannada
description: Detailed description (English)
descriptionKannada: Detailed description (Kannada)
posterUrl: URL to poster image
managerId: Reference to manager who created it
showDate: Date of show (YYYY-MM-DD)
startTime: Start time (HH:MM format)
endTime: End time (calculated)
duration: Duration in minutes
venue: Venue name (English)
venueKannada: Venue name (Kannada)
pricing: Object with front/middle/back prices
totalSeats: Total available seats
seatsBooked: Number of booked seats
rating: Average rating (1-5)
reviewCount: Total number of reviews
status: "ACTIVE", "CANCELLED", "COMPLETED"
createdAt: Creation timestamp
updatedAt: Last update timestamp
Collection/Table: cast_members
JSON

{
  "_id": "cast_1",
  "showId": "show_123",
  "name": "Ravi Kumar",
  "nameKannada": "ರವಿ ಕುಮಾರ್",
  "role": "HERO",
  "character": "Rama",
  "characterKannada": "ರಾಮ",
  "photoUrl": "https://storage.googleapis.com/.../ravi.jpg",
  "category": "LEAD",
  "order": 1
}
Fields:

_id: Unique cast member identifier
showId: Reference to show
name: Cast member name (English)
nameKannada: Cast member name (Kannada)
role: "HERO", "HEROINE", "SINGER", "COMEDIAN", "SUPPORTING"
character: Character name (English)
characterKannada: Character name (Kannada)
photoUrl: URL to photo
category: "LEAD", "SINGER", "COMEDIAN", "SUPPORTING"
order: Display order
Collection/Table: seats
JSON

{
  "_id": "seat_A1",
  "showId": "show_123",
  "row": "A",
  "column": 1,
  "category": "FRONT",
  "price": 100,
  "isBooked": false,
  "bookedBy": null,
  "bookingId": null
}
Fields:

_id: Unique seat identifier (e.g., "seat_A1")
showId: Reference to show
row: Row letter (A-H)
column: Seat number (1-8)
category: "FRONT", "MIDDLE", "BACK"
price: Price for this seat
isBooked: Boolean booking status
bookedBy: User ID if booked
bookingId: Booking reference if booked
Collection/Table: bookings
JSON

{
  "_id": "booking_123",
  "bookingId": "NM12345",
  "userId": "user_123",
  "showId": "show_123",
  "showName": "Ramayana Drama",
  "showDate": "2024-01-20",
  "showTime": "19:00",
  "venue": "Village Square",
  "seats": ["seat_A7", "seat_B4"],
  "seatNumbers": ["A7", "B4"],
  "totalPrice": 200,
  "paymentStatus": "COMPLETED",
  "paymentMethod": "CASH",
  "qrCode": "data:image/png;base64,iVBORw0KGg...",
  "status": "CONFIRMED",
  "bookingDate": "2024-01-20T14:30:00Z",
  "createdAt": "2024-01-20T14:30:00Z"
}
Fields:

_id: Unique booking identifier
bookingId: Human-readable booking ID (e.g., "NM12345")
userId: Reference to user
showId: Reference to show
showName: Cached show name
showDate: Cached show date
showTime: Cached show time
venue: Cached venue
seats: Array of seat IDs
seatNumbers: Array of human-readable seat numbers
totalPrice: Total booking amount
paymentStatus: "PENDING", "COMPLETED", "FAILED"
paymentMethod: "CASH", "UPI", "CARD"
qrCode: Base64 encoded QR code image
status: "CONFIRMED", "CANCELLED"
bookingDate: When booking was made
createdAt: Creation timestamp
Collection/Table: reviews
JSON

{
  "_id": "review_1",
  "showId": "show_123",
  "userId": "user_123",
  "userName": "Rajesh K.",
  "rating": 5,
  "comment": "Amazing performance!",
  "commentKannada": "ಅದ್ಭುತ ಪ್ರದರ್ಶನ!",
  "likes": 24,
  "createdAt": "2024-01-21T22:00:00Z"
}
Fields:

_id: Unique review identifier
showId: Reference to show
userId: Reference to user
userName: Cached user name
rating: Rating (1-5 stars)
comment: Review text (English)
commentKannada: Review text (Kannada)
likes: Number of likes
createdAt: Review timestamp
Collection/Table: comments (Fan Wall)
JSON

{
  "_id": "comment_1",
  "showId": "show_123",
  "userId": "user_123",
  "userName": "Priya S.",
  "userAvatar": "https://...",
  "message": "The singers were superb!",
  "messageKannada": "ಗಾಯಕರು ಅದ್ಭುತವಾಗಿದ್ದರು!",
  "likes": 18,
  "likedBy": ["user_456", "user_789"],
  "createdAt": "2024-01-20T20:15:00Z"
}
Fields:

_id: Unique comment identifier
showId: Reference to show (null for general comments)
userId: Reference to user
userName: Cached user name
userAvatar: User's avatar URL
message: Comment text (English)
messageKannada: Comment text (Kannada)
likes: Number of likes
likedBy: Array of user IDs who liked
createdAt: Comment timestamp
Database Relationships
text

users (1) ──────> (N) bookings
users (1) ──────> (N) reviews
users (1) ──────> (N) comments

managers (1) ────> (N) shows

shows (1) ──────> (N) cast_members
shows (1) ──────> (N) seats
shows (1) ──────> (N) bookings
shows (1) ──────> (N) reviews
shows (1) ──────> (N) comments

bookings (N) ────> (N) seats (many-to-many)
🌐 API REQUIREMENTS & ENDPOINTS
Base URL
text

https://api.nammamela.com/v1
Authentication
All authenticated endpoints require JWT token in header:

text

Authorization: Bearer {token}
Authentication Endpoints
1. Send OTP
http

POST /auth/send-otp
Content-Type: application/json

Request Body:
{
  "phone": "+919876543210"
}

Success Response (200):
{
  "success": true,
  "message": "OTP sent successfully"
}

Error Response (400):
{
  "success": false,
  "error": "Invalid phone number"
}
2. Verify OTP
http

POST /auth/verify-otp
Content-Type: application/json

Request Body:
{
  "phone": "+919876543210",
  "otp": "123456"
}

Success Response (200):
{
  "success": true,
  "userId": "user_123",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "isNewUser": false,
  "user": {
    "id": "user_123",
    "phone": "+919876543210",
    "name": "Rajesh Kumar",
    "languagePreference": "kannada"
  }
}

Error Response (401):
{
  "success": false,
  "error": "Invalid OTP"
}
3. Manager Login
http

POST /auth/manager-login
Content-Type: application/json

Request Body:
{
  "email": "manager@dramacompany.com",
  "password": "securePassword123"
}

Success Response (200):
{
  "success": true,
  "managerId": "mgr_123",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "manager": {
    "id": "mgr_123",
    "email": "manager@dramacompany.com",
    "companyName": "Sri Krishna Drama Company"
  }
}

Error Response (401):
{
  "success": false,
  "error": "Invalid credentials"
}
Show Endpoints
4. Get Today's Shows
http

GET /shows/today
Authorization: Bearer {token}

Success Response (200):
{
  "shows": [
    {
      "id": "show_123",
      "name": "Ramayana Drama",
      "nameKannada": "ರಾಮಾಯಣ ನಾಟಕ",
      "description": "Epic tale...",
      "posterUrl": "https://...",
      "startTime": "19:00",
      "duration": 180,
      "venue": "Village Square",
      "seatsAvailable": 45,
      "totalSeats": 80,
      "priceRange": {
        "min": 20,
        "max": 100
      },
      "rating": 4.8,
      "reviewCount": 125
    }
  ]
}
5. Get Show Details
http

GET /shows/{showId}
Authorization: Bearer {token}

Success Response (200):
{
  "id": "show_123",
  "name": "Ramayana Drama",
  "nameKannada": "ರಾಮಾಯಣ ನಾಟಕ",
  "description": "Epic tale of Lord Rama...",
  "descriptionKannada": "...",
  "posterUrl": "https://...",
  "showDate": "2024-01-20",
  "startTime": "19:00",
  "endTime": "22:00",
  "duration": 180,
  "venue": "Village Square Stage",
  "venueKannada": "ಗ್ರಾಮ ಚೌಕ ವೇದಿಕೆ",
  "pricing": {
    "front": 100,
    "middle": 50,
    "back": 20
  },
  "totalSeats": 80,
  "seatsBooked": 35,
  "rating": 4.8,
  "reviewCount": 125,
  "cast": [
    {
      "id": "cast_1",
      "name": "Ravi Kumar",
      "nameKannada": "ರವಿ ಕುಮಾರ್",
      "role": "HERO",
      "character": "Rama",
      "photoUrl": "https://...",
      "category": "LEAD"
    }
  ]
}

Error Response (404):
{
  "success": false,
  "error": "Show not found"
}
6. Create Show (Manager Only)
http

POST /shows/create
Authorization: Bearer {manager_token}
Content-Type: application/json

Request Body:
{
  "name": "Ramayana Drama",
  "nameKannada": "ರಾಮಾಯಣ ನಾಟಕ",
  "description": "Epic tale...",
  "descriptionKannada": "...",
  "posterUrl": "https://storage.../poster.jpg",
  "showDate": "2024-01-20",
  "startTime": "19:00",
  "duration": 180,
  "venue": "Village Square",
  "venueKannada": "ಗ್ರಾಮ ಚೌಕ",
  "pricing": {
    "front": 100,
    "middle": 50,
    "back": 20
  },
  "cast": [
    {
      "name": "Ravi Kumar",
      "nameKannada": "ರವಿ ಕುಮಾರ್",
      "role": "HERO",
      "character": "Rama",
      "photoUrl": "https://...",
      "category": "LEAD"
    }
  ]
}

Success Response (201):
{
  "success": true,
  "showId": "show_123",
  "message": "Show created successfully"
}

Error Response (403):
{
  "success": false,
  "error": "Unauthorized - Manager access required"
}
7. Update Show (Manager Only)
http

PUT /shows/{showId}/update
Authorization: Bearer {manager_token}
Content-Type: application/json

Request Body:
{
  "name": "Updated Show Name",
  "pricing": {
    "front": 120,
    "middle": 60,
    "back": 25
  }
}

Success Response (200):
{
  "success": true,
  "message": "Show updated successfully"
}
8. Delete Show (Manager Only)
http

DELETE /shows/{showId}
Authorization: Bearer {manager_token}

Success Response (200):
{
  "success": true,
  "message": "Show deleted successfully"
}
Seat & Booking Endpoints
9. Get Seat Availability
http

GET /shows/{showId}/seats
Authorization: Bearer {token}

Success Response (200):
{
  "seats": [
    {
      "id": "seat_A1",
      "row": "A",
      "column": 1,
      "category": "FRONT",
      "price": 100,
      "isBooked": false
    },
    {
      "id": "seat_A2",
      "row": "A",
      "column": 2,
      "category": "FRONT",
      "price": 100,
      "isBooked": true
    }
  ],
  "summary": {
    "total": 80,
    "available": 45,
    "booked": 35
  }
}
10. Create Booking
http

POST /bookings/create
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "showId": "show_123",
  "seats": ["seat_A7", "seat_B4"]
}

Success Response (201):
{
  "success": true,
  "bookingId": "booking_123",
  "bookingCode": "NM12345",
  "qrCode": "data:image/png;base64,iVBORw0KGg...",
  "booking": {
    "id": "booking_123",
    "showName": "Ramayana Drama",
    "showDate": "2024-01-20",
    "showTime": "19:00",
    "venue": "Village Square",
    "seats": ["A7", "B4"],
    "totalPrice": 200,
    "status": "CONFIRMED"
  }
}

Error Response (409):
{
  "success": false,
  "error": "One or more seats already booked",
  "unavailableSeats": ["seat_A7"]
}
11. Get User Bookings
http

GET /bookings/user
Authorization: Bearer {token}
Query Parameters:
  - status (optional): "CONFIRMED" | "CANCELLED"
  - page (optional): page number
  - limit (optional): items per page

Success Response (200):
{
  "bookings": [
    {
      "id": "booking_123",
      "bookingCode": "NM12345",
      "showName": "Ramayana Drama",
      "showDate": "2024-01-20",
      "showTime": "19:00",
      "venue": "Village Square",
      "seats": ["A7", "B4"],
      "totalPrice": 200,
      "status": "CONFIRMED",
      "qrCode": "data:image/png;base64...",
      "bookingDate": "2024-01-20T14:30:00Z"
    }
  ],
  "pagination": {
    "currentPage": 1,
    "totalPages": 3,
    "totalItems": 25
  }
}
12. Get Booking Details
http

GET /bookings/{bookingId}
Authorization: Bearer {token}

Success Response (200):
{
  "id": "booking_123",
  "bookingCode": "NM12345",
  "showName": "Ramayana Drama",
  "showDate": "2024-01-20",
  "showTime": "19:00",
  "venue": "Village Square",
  "seats": ["A7", "B4"],
  "totalPrice": 200,
  "status": "CONFIRMED",
  "qrCode": "data:image/png;base64...",
  "bookingDate": "2024-01-20T14:30:00Z"
}
Review & Comment Endpoints
13. Get Reviews
http

GET /reviews?showId={showId}&page=1&limit=20
Authorization: Bearer {token}

Success Response (200):
{
  "reviews": [
    {
      "id": "review_1",
      "userId": "user_123",
      "userName": "Rajesh K.",
      "rating": 5,
      "comment": "Amazing performance!",
      "likes": 24,
      "createdAt": "2024-01-21T22:00:00Z"
    }
  ],
  "averageRating": 4.8,
  "totalReviews": 125,
  "ratingBreakdown": {
    "5": 80,
    "4": 30,
    "3": 10,
    "2": 3,
    "1": 2
  }
}
14. Post Review
http

POST /reviews/create
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "showId": "show_123",
  "rating": 5,
  "comment": "Amazing performance!"
}

Success Response (201):
{
  "success": true,
  "reviewId": "review_456",
  "message": "Review posted successfully"
}

Error Response (400):
{
  "success": false,
  "error": "You must book a ticket to review this show"
}
15. Get Fan Wall Comments
http

GET /comments?showId={showId}&page=1&limit=20
Authorization: Bearer {token}

Success Response (200):
{
  "comments": [
    {
      "id": "comment_1",
      "userId": "user_123",
      "userName": "Ramesh K.",
      "userAvatar": "https://...",
      "message": "Amazing performance!",
      "likes": 24,
      "isLikedByUser": false,
      "createdAt": "2024-01-20T20:00:00Z"
    }
  ],
  "hasMore": true
}
16. Post Comment
http

POST /comments/create
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "showId": "show_123",
  "message": "Loved the show!"
}

Success Response (201):
{
  "success": true,
  "commentId": "comment_456",
  "message": "Comment posted successfully"
}
17. Like/Unlike Comment
http

POST /comments/{commentId}/like
Authorization: Bearer {token}

Success Response (200):
{
  "success": true,
  "isLiked": true,
  "likes": 25
}
Manager Dashboard Endpoints
18. Get Dashboard Stats
http

GET /manager/dashboard
Authorization: Bearer {manager_token}

Success Response (200):
{
  "todayBookings": 47,
  "totalSeats": 80,
  "revenue": 3250,
  "activeShows": 5,
  "upcomingShows": [
    {
      "id": "show_123",
      "name": "Ramayana Drama",
      "showDate": "2024-01-20",
      "showTime": "19:00",
      "bookings": 47,
      "revenue": 3250,
      "seatsAvailable": 33
    }
  ],
  "recentBookings": [
    {
      "bookingCode": "NM12345",
      "customerName": "Rajesh K.",
      "showName": "Ramayana Drama",
      "seats": 2,
      "amount": 200,
      "bookingTime": "2024-01-20T14:30:00Z"
    }
  ]
}
Upload Endpoint
19. Upload Image
http

POST /upload/image
Authorization: Bearer {token}
Content-Type: multipart/form-data

Request Body:
FormData with:
  - image: File (JPEG/PNG, max 5MB)
  - type: "poster" | "cast" | "avatar"

Success Response (200):
{
  "success": true,
  "imageUrl": "https://storage.googleapis.com/.../image.jpg",
  "thumbnailUrl": "https://storage.googleapis.com/.../thumb_image.jpg"
}

Error Response (400):
{
  "success": false,
  "error": "File too large. Maximum size is 5MB"
}
🎨 DESIGN GUIDELINES & UI/UX SPECIFICATIONS
Color Palette
Primary Colors
CSS

Deep Maroon:   #8B0000  /* Main brand color, curtains */
Gold:          #FFD700  /* Accents, highlights */
Cream:         #FFF8DC  /* Backgrounds */
Functional Colors
CSS

Green:         #4CAF50  /* Available seats, success */
Red:           #F44336  /* Booked seats, errors */
Yellow:        #FFC107  /* Selected seats, warnings */
Blue:          #2196F3  /* Information, links */
Text Colors
CSS

Primary:       #212121  /* Dark gray - main text */
Secondary:     #757575  /* Medium gray - secondary text */
On Dark:       #FFFFFF  /* White - text on dark backgrounds */
Disabled:      #BDBDBD  /* Light gray - disabled elements */
Typography
Font Family: Roboto (default Android font) + Noto Sans Kannada

Text Styles:

text

Heading 1:     24sp, Bold, Letter spacing 0.15
Heading 2:     20sp, Medium, Letter spacing 0.15
Heading 3:     18sp, Medium, Letter spacing 0.15
Body:          14sp, Regular, Letter spacing 0.25
Caption:       12sp, Light, Letter spacing 0.4
Button:        16sp, Medium, UPPERCASE, Letter spacing 1.25
Kannada Font: Noto Sans Kannada (Google Fonts)

Spacing System
text

Micro:         4dp
Small:         8dp
Medium:        16dp
Large:         24dp
XLarge:        32dp
XXLarge:       48dp
Standard Padding: 16dp
Card Gaps: 12dp
Margin Between Elements: 8dp

Component Sizes
text

Button Height:         48dp (minimum touch target)
Icon Size:             24dp × 24dp
Large Icon:            48dp × 48dp
Poster Card:           160dp × 240dp
Cast Avatar:           80dp × 80dp (circular)
Seat Button:           32dp × 32dp
Bottom Navigation:     56dp height
App Bar:               56dp height
FAB:                   56dp diameter
Input Field Height:    56dp
Design Principles
1. Cultural Authenticity
Use traditional Indian motifs (mandalas, paisley patterns)
Incorporate drama-related imagery (masks 🎭, curtains)
Use warm, theatrical colors (maroon, gold)
Include traditional border designs
2. Accessibility
Large touch targets (minimum 48dp)
High contrast ratios (4.5:1 for text, 3:1 for UI elements)
Clear icon labels
Support for larger font sizes (up to 200%)
Color is not the only indicator (use icons + text)
3. Rural-Friendly Design
Minimal text input (use pickers, dropdowns)
Visual indicators (icons, images)
Simple navigation (bottom nav always visible)
Offline capability for viewing bookings
Low bandwidth support (compressed images)
4. Bilingual Support
All text in English and Kannada
Font support: Noto Sans Kannada
Right-to-left (RTL) not required (Kannada is LTR)
Language toggle easily accessible
UI Components Library
Buttons
Primary Button:

text

Background: #8B0000 (Maroon)
Text: #FFFFFF (White)
Height: 48dp
Corner Radius: 8dp
Text: 16sp, Medium, UPPERCASE
Elevation: 4dp
Secondary Button:

text

Background: Transparent
Border: 2dp solid #8B0000
Text: #8B0000
Height: 48dp
Corner Radius: 8dp
Icon Button:

text

Size: 48dp × 48dp
Icon: 24dp × 24dp
Background: Ripple effect only
Cards
Show Card:

text

Width: match_parent
Height: wrap_content
Corner Radius: 12dp
Elevation: 4dp
Padding: 12dp
Margin: 8dp
Background: #FFFFFF
Cast Card:

text

Width: 120dp
Height: wrap_content
Corner Radius: 8dp
Elevation: 2dp
Padding: 8dp
Background: #FFFFFF
Input Fields
Text Input:

text

Height: 56dp
Corner Radius: 4dp
Border: 1dp solid #BDBDBD
Focused Border: 2dp solid #8B0000
Padding: 16dp horizontal
Font: 14sp, Regular
Hint Color: #757575
Bottom Navigation
text

Height: 56dp
Background: #FFFFFF
Elevation: 8dp
Active Color: #8B0000
Inactive Color: #757575
Icon Size: 24dp
Label: 12sp, Medium
Screen Templates
Standard Screen Layout
text

┌─────────────────────────────┐
│     App Bar (56dp)          │
├─────────────────────────────┤
│                             │
│                             │
│     Scrollable Content      │
│     (Padding: 16dp)         │
│                             │
│                             │
├─────────────────────────────┤
│  Bottom Navigation (56dp)   │
└─────────────────────────────┘
Full Screen Layout (e.g., Seat Selection)
text

┌─────────────────────────────┐
│     App Bar (56dp)          │
├─────────────────────────────┤
│                             │
│                             │
│     Full Content Area       │
│     (No side padding)       │
│                             │
│                             │
├─────────────────────────────┤
│  Fixed Bottom CTA (72dp)    │
└─────────────────────────────┘
✨ FEATURES & FUNCTIONALITY BREAKDOWN
Customer Features
✅ Must-Have (MVP - Phase 1)
Phone OTP Authentication

Send OTP via Firebase
Verify OTP
Auto-fill OTP (SMS Retriever API)
View Tonight's Shows

List all shows for today
Show poster, time, venue, availability
Pull to refresh
View Show Details

Complete show information
Cast preview
Pricing details
Reviews summary
Visual Seat Selection

Interactive seat map
Color-coded availability
Multi-select (up to 10 seats)
Real-time price calculation
Book Tickets

Confirm booking
Generate QR code
Save to booking history
Language Toggle

English/Kannada switch
Persist preference
Apply to all screens
View Cast Information

Categorized by role
Photos and names
Character information
Rate and Review Shows

5-star rating
Text review
View others' reviews
Fan Wall Comments

Post comments
Like comments
View community feed
Booking Confirmation

QR code display
Ticket details
Download option
🎯 Should-Have (Phase 2)
View Booking History

Past bookings
Upcoming bookings
Booking details
Download Ticket

Save as image
Share via WhatsApp/Email
Share Ticket

Share booking confirmation
Social media integration
Push Notifications

Booking confirmation
Show reminders (1 hour before)
New show alerts
Favorite Shows

Mark shows as favorite
View favorites list
Get notified about favorites
Filter Shows

By date
By genre (future)
By location
Search Functionality

Search by show name
Search by cast name
Edit Profile

Update name
Change language preference
Add email (optional)
💡 Could-Have (Future Enhancements)
Payment Integration

UPI payments (Razorpay/Paytm)
Online booking payment
Payment history
Seat Recommendations

Best available seats
AI-based suggestions
Video Trailers

Watch show trailers
Behind-the-scenes videos
Live Show Updates

Real-time seat availability
Show status (delayed/cancelled)
Chat with Cast

Message feature
Q&A sessions
Loyalty Points

Earn points on bookings
Redeem for discounts
Group Booking

Book for multiple people
Split payment
Voice Search

Search by voice (Kannada/English)
Voice commands
Manager Features
✅ Must-Have (MVP - Phase 1)
Email Login

Secure email/password auth
Password recovery
Add New Shows

Complete show details
Upload poster
Set pricing
Add cast members
Edit Show Details

Update any field
Replace poster
Modify cast
Delete Shows

Remove shows
Confirmation dialog
Upload Posters

Image upload
Image preview
Compression
Add/Manage Cast

Add cast members
Assign roles
Upload photos
Remove cast
View Booking Statistics

Today's bookings
Revenue
Seat occupancy
View Revenue

Daily revenue
Per-show revenue
🎯 Should-Have (Phase 2)
Analytics Dashboard

Charts and graphs
Booking trends
Popular shows
Export Booking Reports

CSV/Excel export
PDF reports
Email reports
Manage Multiple Venues

Add venues
Assign shows to venues
Bulk Seat Reset

Reset all seats for new day
Archive old bookings
Send Notifications

Notify customers
Promotional messages
View Customer Reviews

See all reviews
Respond to reviews
💡 Could-Have (Future Enhancements)
Advanced Analytics

Predictive analytics
Customer demographics
Revenue forecasting
Revenue Forecasting

AI-based predictions
Trend analysis
Marketing Campaigns

Create promotions
Discount codes
SMS campaigns
Multi-Manager Support

Multiple manager accounts
Role-based access
Role-Based Permissions

Admin, Editor, Viewer roles
Granular permissions
💻 TECHNICAL STACK & DEPENDENCIES
Android App Specifications
Language: Kotlin
Min SDK: 24 (Android 7.0 - Nougat)
Target SDK: 34 (Android 14)
Build Tool: Gradle 8.0+

Required Libraries
Core Android Libraries
gradle

dependencies {
    // Kotlin
    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.9.20"
    
    // AndroidX Core
    implementation "androidx.core:core-ktx:1.12.0"
    implementation "androidx.appcompat:appcompat:1.6.1"
    
    // Material Design
    implementation "com.google.android.material:material:1.11.0"
    
    // ConstraintLayout
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
}
Navigation & UI
gradle

dependencies {
    // Jetpack Navigation
    implementation "androidx.navigation:navigation-fragment-ktx:2.7.6"
    implementation "androidx.navigation:navigation-ui-ktx:2.7.6"
    
    // RecyclerView
    implementation "androidx.recyclerview:recyclerview:1.3.2"
    
    // ViewPager2
    implementation "androidx.viewpager2:viewpager2:1.0.0"
    
    // CardView
    implementation "androidx.cardview:cardview:1.0.0"
    
    // SwipeRefreshLayout
    implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
}
Networking
gradle

dependencies {
    // Retrofit (REST API)
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    
    // OkHttp (HTTP Client)
    implementation "com.squareup.okhttp3:okhttp:4.12.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"
    
    // Gson (JSON Parsing)
    implementation "com.google.code.gson:gson:2.10.1"
}
Image Loading
gradle

dependencies {
    // Glide
    implementation "com.github.bumptech.glide:glide:4.16.0"
    kapt "com.github.bumptech.glide:compiler:4.16.0"
    
    // Alternative: Coil
    // implementation "io.coil-kt:coil:2.5.0"
}
Local Database
gradle

dependencies {
    // Room Database
    implementation "androidx.room:room-runtime:2.6.1"
    implementation "androidx.room:room-ktx:2.6.1"
    kapt "androidx.room:room-compiler:2.6.1"
}
Firebase
gradle

dependencies {
    // Firebase BOM (Bill of Materials)
    implementation platform("com.google.firebase:firebase-bom:32.7.0")
    
    // Firebase Authentication
    implementation "com.google.firebase:firebase-auth-ktx"
    
    // Firebase Cloud Messaging
    implementation "com.google.firebase:firebase-messaging-ktx"
    
    // Firebase Storage
    implementation "com.google.firebase:firebase-storage-ktx"
    
    // Firebase Firestore (optional - if using Firebase as backend)
    implementation "com.google.firebase:firebase-firestore-ktx"
}
QR Code Generation
gradle

dependencies {
    // ZXing (QR Code)
    implementation "com.google.zxing:core:3.5.2"
    implementation "com.journeyapps:zxing-android-embedded:4.3.0"
}
Dependency Injection
gradle

dependencies {
    // Hilt (Recommended)
    implementation "com.google.dagger:hilt-android:2.50"
    kapt "com.google.dagger:hilt-compiler:2.50"
    
    // Alternative: Koin
    // implementation "io.insert-koin:koin-android:3.5.0"
}
Lifecycle & ViewModel
gradle

dependencies {
    // Lifecycle Components
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0"
    
    // Activity & Fragment Extensions
    implementation "androidx.activity:activity-ktx:1.8.2"
    implementation "androidx.fragment:fragment-ktx:1.6.2"
}
Coroutines
gradle

dependencies {
    // Kotlin Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"
}
Data Storage
gradle

dependencies {
    // DataStore (SharedPreferences replacement)
    implementation "androidx.datastore:datastore-preferences:1.0.0"
    
    // Encrypted SharedPreferences
    implementation "androidx.security:security-crypto:1.1.0-alpha06"
}
Testing
gradle

dependencies {
    // JUnit
    testImplementation "junit:junit:4.13.2"
    
    // AndroidX Test
    androidTestImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
    
    // Mockito
    testImplementation "org.mockito:mockito-core:5.7.0"
    testImplementation "org.mockito.kotlin:mockito-kotlin:5.2.1"
    
    // Coroutines Test
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
}
Utilities
gradle

dependencies {
    // Lottie (Animations)
    implementation "com.airbnb.android:lottie:6.3.0"
    
    // Timber (Logging)
    implementation "com.jakewharton.timber:timber:5.0.1"
    
    // LeakCanary (Memory Leak Detection - Debug only)
    debugImplementation "com.squareup.leakcanary:leakcanary-android:2.12"
}
Backend Stack Options
Option 1: Node.js + Express (Recommended)
text

- Runtime: Node.js 18+
- Framework: Express.js
- Database: MongoDB + Mongoose
- Authentication: jsonwebtoken (JWT)
- File Upload: Multer
- Image Storage: Firebase Storage / AWS S3
- SMS/OTP: Twilio / Firebase Auth
- Deployment: Heroku / Railway / DigitalOcean
Key Packages:

JSON

{
  "dependencies": {
    "express": "^4.18.2",
    "mongoose": "^8.0.3",
    "jsonwebtoken": "^9.0.2",
    "bcryptjs": "^2.4.3",
    "multer": "^1.4.5-lts.1",
    "cors": "^2.8.5",
    "dotenv": "^16.3.1",
    "firebase-admin": "^12.0.0",
    "twilio": "^4.20.0"
  }
}
Option 2: Firebase (Serverless - Fastest Development)
text

- Database: Cloud Firestore
- API: Cloud Functions (JavaScript/TypeScript)
- Authentication: Firebase Authentication
- Storage: Cloud Storage
- Hosting: Firebase Hosting
- Notifications: Cloud Messaging
Advantages:

No server management
Auto-scaling
Built-in security rules
Real-time updates
Free tier available
Option 3: Django/Flask (Python)
text

- Framework: Django REST Framework / Flask
- Database: PostgreSQL / MongoDB
- Authentication: Django Rest Framework Auth / Flask-JWT
- File Storage: AWS S3 / Cloudinary
- Deployment: PythonAnywhere / Heroku
Project Structure (Android)
text

app/
├── src/
│   ├── main/
│   │   ├── java/com/nammamela/
│   │   │   ├── data/
│   │   │   │   ├── models/          # Data classes
│   │   │   │   ├── repository/      # Data repository
│   │   │   │   ├── local/           # Room database
│   │   │   │   │   ├── dao/
│   │   │   │   │   ├── entities/
│   │   │   │   │   └── database/
│   │   │   │   └── remote/          # API services
│   │   │   │       ├── api/
│   │   │   │       └── dto/
│   │   │   ├── di/                  # Dependency injection
│   │   │   ├── ui/
│   │   │   │   ├── auth/            # Authentication screens
│   │   │   │   │   ├── login/
│   │   │   │   │   └── otp/
│   │   │   │   ├── home/            # Home screen
│   │   │   │   ├── detail/          # Show detail
│   │   │   │   ├── cast/            # Cast screen
│   │   │   │   ├── booking/         # Seat selection & booking
│   │   │   │   ├── fanwall/         # Fan wall
│   │   │   │   ├── manager/         # Manager screens
│   │   │   │   └── common/          # Shared UI components
│   │   │   ├── viewmodel/           # ViewModels
│   │   │   ├── utils/               # Utility classes
│   │   │   │   ├── Constants.kt
│   │   │   │   ├── Extensions.kt
│   │   │   │   └── DateUtils.kt
│   │   │   └── NammaMelaApp.kt     # Application class
│   │   ├── res/
│   │   │   ├── layout/              # XML layouts
│   │   │   ├── drawable/            # Images & icons
│   │   │   ├── values/              # Strings, colors, styles
│   │   │   │   ├── strings.xml      # English strings
│   │   │   │   ├── colors.xml
│   │   │   │   ├── styles.xml
│   │   │   │   └── themes.xml
│   │   │   ├── values-kn/           # Kannada strings
│   │   │   │   └── strings.xml
│   │   │   ├── navigation/          # Navigation graph
│   │   │   └── menu/                # Menu items
│   │   └── AndroidManifest.xml
│   └── test/                        # Unit tests
│   └── androidTest/                 # Instrumented tests
├── build.gradle                     # App-level Gradle
└── proguard-rules.pro              # ProGuard rules
🚀 DEVELOPMENT PHASES & MILESTONES
Phase 1: Foundation (Week 1-2)
Tasks
 Set up Android project in Android Studio
 Add all required dependencies
 Set up Hilt dependency injection
 Create base Activity/Fragment classes
 Implement navigation graph
 Design database schema
 Create data models (DTOs, Entities)
 Set up Retrofit for API calls
 Implement Room database
 Create repository pattern structure
 Set up Firebase project
Deliverable
App skeleton with navigation
Database structure ready
API service layer ready
Phase 2: Authentication (Week 2-3)
Tasks
 Create splash screen
 Implement language selection screen
 Implement role selection screen
 Integrate Firebase Phone Auth
 Create OTP verification screen
 Implement auto-fill OTP (SMS Retriever API)
 Implement manager email login
 Set up JWT token management
 Implement session persistence (DataStore)
 Create user profile storage
Deliverable
Working authentication flow for both users
Language preference saved
Auto-login on app restart
Phase 3: Customer Features - Browsing (Week 3-4)
Tasks
 Create Home screen UI
 Implement show list RecyclerView
 Fetch shows from API
 Implement pull-to-refresh
 Create show detail screen
 Load show details from API
 Display poster with Glide
 Show pricing information
 Implement favorite functionality
 Create cast display screen
 Load cast members
 Implement categorized cast display
Deliverable
Complete browsing experience
Show list → Detail → Cast flow working
Phase 4: Customer Features - Booking (Week 4-5)
Tasks
 Design seat selection UI
 Implement seat grid layout
 Fetch seat availability from API
 Implement seat selection logic
 Add color coding (available/booked/selected)
 Calculate total price dynamically
 Implement seat booking API call
 Handle booking conflicts
 Create booking confirmation screen
 Generate QR code using ZXing
 Implement ticket download
 Implement share functionality
Deliverable
Complete booking flow
QR code ticket generation
Booking saved to history
Phase 5: Manager Features (Week 5-6)
Tasks
 Create manager dashboard UI
 Fetch and display stats
 Create add show screen
 Implement form validation
 Upload poster to Firebase Storage
 Submit show data to API
 Create edit show screen
 Prefill form with existing data
 Implement update functionality
 Create cast management screen
 Add/remove cast members
 Implement delete show with confirmation
 Create booking list view
Deliverable
Complete manager panel
CRUD operations for shows
Cast management working
Phase 6: Social Features (Week 6-7)
Tasks
 Create review/rating UI
 Implement star rating component
 Submit review to API
 Display reviews list
 Implement review likes
 Create fan wall screen
 Implement comment posting
 Load comments with pagination
 Implement like/unlike comments
 Create user profile screen
 Display booking history
 Implement profile editing
Deliverable
Social features working
Review and comment system live
User profile management
Phase 7: Polish & Optimization (Week 7-8)
Tasks
 Implement bilingual support completely
 Add loading states to all screens
 Implement error handling
 Add offline mode for viewing bookings
 Implement push notifications
 Set up FCM
 Create notification handlers
 Performance optimization
 Image optimization
 Network caching
 Memory leak fixes
 UI/UX refinements
 Animations
 Transitions
 Empty states
 Error states
 Accessibility improvements
Deliverable
Polished, production-ready app
All edge cases handled
Smooth user experience
Phase 8: Testing & Deployment (Week 8)
Tasks
 Write unit tests for ViewModels
 Write unit tests for repositories
 Write integration tests for API
 Write UI tests for critical flows
 Manual testing on multiple devices
 Test on different Android versions
 Test with slow internet
 Test offline functionality
 Security testing
 Performance testing
 Beta testing with real users
 Fix bugs from testing
 Prepare release build
 Generate signed APK
 Create Play Store listing (optional)
Deliverable
Fully tested app
Release APK
Documentation
🧪 TESTING REQUIREMENTS
Unit Tests
Areas to Test
 ViewModel logic
 Repository functions
 Data transformations
 Validation functions
 Date/time utilities
 Business logic
Example Tests
Kotlin

class BookingViewModelTest {
    
    @Test
    fun `calculate total price correctly`() {
        val seats = listOf(
            Seat("A", 1, "FRONT", 100),
            Seat("B", 4, "FRONT", 100)
        )
        val total = viewModel.calculateTotal(seats)
        assertEquals(200, total)
    }
    
    @Test
    fun `validate seat selection limit`() {
        val selectedSeats = List(11) { Seat("A", it, "FRONT", 100) }
        val isValid = viewModel.validateSeats(selectedSeats)
        assertFalse(isValid)
    }
}
Integration Tests
Areas to Test
 API calls (using MockWebServer)
 Database operations (Room)
 Authentication flow
 Booking creation flow
 Image upload
Example Tests
Kotlin

class ShowRepositoryTest {
    
    @Test
    fun `fetch today's shows from API`() = runBlocking {
        val shows = repository.getTodayShows()
        assertTrue(shows.isNotEmpty())
        assertEquals("Ramayana Drama", shows[0].name)
    }
}
UI Tests (Espresso)
Critical Flows to Test
 Login flow (OTP)
 Booking flow (end-to-end)
 Add show flow (manager)
 Navigation between screens
Example Tests
Kotlin

@RunWith(AndroidJUnit4::class)
class BookingFlowTest {
    
    @Test
    fun testCompleteBookingFlow() {
        // Navigate to home
        onView(withId(R.id.nav_home)).perform(click())
        
        // Click on first show
        onView(withId(R.id.recycler_shows))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        
        // Click book button
        onView(withId(R.id.btn_book_seats)).perform(click())
        
        // Select seat A7
        onView(withId(R.id.seat_A7)).perform(click())
        
        // Confirm booking
        onView(withId(R.id.btn_confirm_booking)).perform(click())
        
        // Verify success screen
        onView(withId(R.id.tv_booking_success))
            .check(matches(isDisplayed()))
    }
}
Manual Testing Checklist
Before Each Release
 Test on different screen sizes (small, normal, large, xlarge)
 Test on different Android versions (7.0 to 14)
 Test with slow internet (throttle network)
 Test offline mode
 Test with different languages (English & Kannada)
 Test edge cases:
 No shows available
 All seats booked
 Invalid OTP
 Network errors
 Server errors
 Test payment flows (when implemented)
 Test push notifications
 Test deep links (if any)
 Security testing:
 Token expiration
 SQL injection prevention
 XSS prevention
 Performance testing:
 App startup time
 Screen load time
 Memory usage
 Battery consumption
📱 ADDITIONAL REQUIREMENTS
Android Manifest Permissions
XML

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.nammamela.app">

    <!-- Internet & Network -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    
    <!-- Camera (for QR scanning - future) -->
    <uses-permission android:name="android.permission.CAMERA" />
    
    <!-- Storage (for downloading tickets) -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />
    
    <!-- Notifications -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    
    <!-- SMS (for OTP auto-fill) -->
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.READ_SMS" />
    
    <application
        android:name=".NammaMelaApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="false"
        android:theme="@style/Theme.NammaMela"
        android:usesCleartextTraffic="false">
        
        <!-- Activities -->
        <activity
            android:name=".ui.MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <!-- Firebase Messaging Service -->
        <service
            android:name=".services.MyFirebaseMessagingService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
        
    </application>

</manifest>
Performance Requirements
App Performance Targets
App Startup Time: < 3 seconds
Screen Transition: < 300ms
API Response Handling: Show loading state within 100ms
Image Loading: Progressive with placeholders
Offline Support: Cache show data for 24 hours
Memory Usage: < 150MB on average
APK Size: < 25MB
Optimization Techniques
Image Optimization

Use WebP format
Compress images before upload
Use Glide's thumbnail loading
Network Optimization

Implement caching (OkHttp Cache)
Use pagination for lists
Compress API responses (gzip)
Database Optimization

Index frequently queried fields
Use database queries on background thread
Limit query results
Security Requirements
1. Authentication Security
JWT tokens expire in 7 days
Implement refresh token mechanism
Secure token storage using EncryptedSharedPreferences
Validate tokens on every API call
Kotlin

// Example: Secure token storage
val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val encryptedPrefs = EncryptedSharedPreferences.create(
    context,
    "secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
2. Network Security
HTTPS only for all API calls
Certificate pinning (optional for high security)
Disable clear text traffic
XML

<!-- network_security_config.xml -->
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
</network-security-config>
3. Data Security
Input validation on all forms
SQL injection prevention (Room handles this)
XSS protection
Encrypt sensitive data in local database
4. Payment Security (Future)
PCI-DSS compliance if handling cards
Use payment gateway SDKs (Razorpay/Paytm)
Never store card details locally
Implement 2-factor authentication for payments
Localization (Bilingual Support)
Implementation Steps
Create String Resources
res/values/strings.xml (English)

XML

<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Namma-Mela</string>
    <string name="welcome">Welcome to Namma-Mela</string>
    <string name="book_now">Book Now</string>
    <string name="select_seats">Select Seats</string>
    <string name="confirm_booking">Confirm Booking</string>
    <string name="tonight_shows">Tonight\'s Shows</string>
    <string name="cast_members">Cast Members</string>
    <string name="fan_wall">Fan Wall</string>
</resources>
res/values-kn/strings.xml (Kannada)

XML

<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">ನಮ್ಮ ಮೇಳ</string>
    <string name="welcome">ನಮ್ಮ ಮೇಳಕ್ಕೆ ಸ್ವಾಗತ</string>
    <string name="book_now">ಈಗ ಬುಕ್ ಮಾಡಿ</string>
    <string name="select_seats">ಸೀಟುಗಳನ್ನು ಆಯ್ಕೆಮಾಡಿ</string>
    <string name="confirm_booking">ಬುಕಿಂಗ್ ದೃಢೀಕರಿಸಿ</string>
    <string name="tonight_shows">ಇಂದಿನ ಪ್ರದರ್ಶನಗಳು</string>
    <string name="cast_members">ಪಾತ್ರವರ್ಗ</string>
    <string name="fan_wall">ಅಭಿಮಾನಿಗಳ ಗೋಡೆ</string>
</resources>
Apply Locale Programmatically
Kotlin

fun setAppLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    
    val config = context.resources.configuration
    config.setLocale(locale)
    
    context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}

// Usage
setAppLocale(context, "kn") // Kannada
setAppLocale(context, "en") // English
Store Language Preference
Kotlin

// Save preference
dataStore.edit { preferences ->
    preferences[LANGUAGE_KEY] = "kannada"
}

// Retrieve and apply on app start
val languageFlow: Flow<String> = dataStore.data.map { preferences ->
    preferences[LANGUAGE_KEY] ?: "english"
}
Offline Functionality
What Should Work Offline
View booking history
View downloaded tickets
View previously loaded shows (cached)
Switch language
Implementation
Kotlin

// Cache shows using Room
@Entity(tableName = "cached_shows")
data class CachedShow(
    @PrimaryKey val id: String,
    val data: String, // JSON string of show
    val cachedAt: Long
)

// Check cache validity (24 hours)
fun isCacheValid(cachedAt: Long): Boolean {
    val now = System.currentTimeMillis()
    val cacheAge = now - cachedAt
    return cacheAge < TimeUnit.HOURS.toMillis(24)
}
✅ FINAL CHECKLIST FOR DEVELOPMENT TEAM
Pre-Development Setup
 Review complete SOP document
 Set up development environment
 Android Studio installed
 JDK 17+ installed
 Android SDK updated
 Create Firebase project
 Enable Authentication
 Enable Cloud Messaging
 Enable Cloud Storage
 Set up backend server/database
 Choose backend stack
 Set up database (MongoDB/Firestore)
 Deploy initial API
 Create Figma designs (optional but recommended)
 Initialize Git repository
 Create .gitignore
 Set up branches (main, develop)
During Development
 Follow screen-by-screen implementation order
 Write unit tests alongside code (TDD approach)
 Document any deviations from SOP
 Regular Git commits with clear messages
 Code reviews before merging
 Weekly progress reports
 Update README.md with setup instructions
Testing Phase
 Complete all unit tests
 Complete integration tests
 Complete UI tests
 Manual testing on real devices
 Test on minimum supported Android version (7.0)
 Test on latest Android version (14)
 Test all edge cases
 Security testing
 Performance testing
 Beta testing with 10-20 users
Pre-Release
 Generate signed APK
 Create keystore
 Configure signing in build.gradle
 Build release APK
 Prepare demo data
 5-10 sample shows
 20-30 sample reviews
 Sample cast photos
 Create user manual (PDF)
 Create admin guide for managers
 Prepare demo video (3-5 minutes)
 Create presentation slides
Deployment
 Deploy backend to production
 Set up production database
 Configure environment variables
 Set up monitoring (optional)
 Test production API
 Upload APK to Play Store (optional)
 Create app listing
 Add screenshots
 Write description
 Submit for review
📄 DELIVERABLES
Hand Over to Client
✅ APK File

Release build signed APK
Debug APK for testing
✅ Source Code

Complete Android project
Backend source code
Database schema files
Git repository link
✅ Documentation

API documentation (Postman collection or Swagger)
Database schema document
README.md with setup instructions
Code comments and documentation
✅ User Manual

Customer app guide (PDF)
Manager app guide (PDF)
Screenshots and walkthroughs
✅ Demo Assets

Demo video (MP4)
Presentation slides (PPT/PDF)
Sample data (JSON)
✅ Credentials

Firebase project access
Backend admin credentials
Database access
API keys (if any)
🔒 CONFIDENTIALITY & TERMS
Code ownership transfers to client upon final payment
All assets must be properly licensed
No third-party tracking without user consent
GDPR compliance for user data
Right to use project in portfolio (with client permission)
📞 PROJECT CONTACTS
Client: [Your Name]
Development Team: Anti Gravity
Project Timeline: 8 weeks
Budget: [To be decided]
Start Date: [To be decided]
Expected Completion: [To be decided]

🎭 FINAL NOTES
This comprehensive SOP document provides complete specifications for building the Namma-Mela app. It includes:

✅ Detailed screen specifications
✅ Complete API documentation
✅ Database schema
✅ Technical stack recommendations
✅ Development phases
✅ Testing requirements
✅ Security guidelines
✅ Performance targets

Key Success Factors
Simplicity First

Focus on ease of use for rural users
Large buttons, clear icons
Minimal text input
Cultural Authenticity

Use traditional Indian design elements
Support for Kannada language
Drama-themed UI
Offline Support

Cache booking data
View tickets offline
Graceful degradation
Performance

Fast loading times
Smooth animations
Low memory usage
Testing

Test on real devices
Beta testing with actual users
Fix bugs promptly
Development Approach
Follow Agile methodology with:

2-week sprints
Daily standups (if team size > 1)
Weekly demos to client
Continuous integration
Communication
Weekly progress updates
Immediate notification of blockers
Document all decisions
Maintain change log
🌟 VISION
Namma-Mela aims to digitize and preserve the beautiful tradition of Indian drama performances, making it accessible to modern audiences while honoring cultural heritage.

Good luck building this amazing app! 🎭📱✨

Document Version: 1.0
Last Updated: January 2024
Format: README.md (Markdown)

END OF DOCUMENT

text


This complete README.md file is ready to use! You can:

1. **Save it as README.md** in your project root
2. **Share directly with Anti Gravity team** via GitHub/Email
3. **Convert to PDF** using any markdown-to-PDF converter
4. **Use as project documentation** throughout development

The document is structured, comprehensive, and includes everything needed to build the Namma-Mela app from scratch! 🚀





give