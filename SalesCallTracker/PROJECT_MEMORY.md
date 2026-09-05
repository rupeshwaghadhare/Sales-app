# SalesCallTracker — Project Memory / Product Source of Truth

> **Purpose:** Persistent project memory for the SalesCallTracker evolution.
> This file is intended to live in the repository and be treated as the primary product/architecture context before making changes.
>
> **Rule:** Do not remove, forget, or replace previously agreed capabilities just because a new feature is being implemented. Add new requirements to this document instead.

---

## 1. Project Identity

- Repository/project: `SalesCallTracker`
- GitHub repository: `rupeshwagdhare/Sales-app`
- Current Android project path used on Windows:
  `C:\Users\Admin\Documents\GitHub\Sales-app\SalesCallTracker`
- Current working branch mentioned: `v1.0-interactive-ui`
- App name/repository name should remain **SalesCallTracker**.
- Do not spend project work on renaming/branding unless explicitly requested.
- The project started as an Android call-log/sales tracking app and is evolving into a much broader business platform.
- The existing call-log functionality is foundational and must not be broken.

---

## 2. Core Product Vision

SalesCallTracker should evolve into a unified, local-first:

**Business Marketplace + CRM + Sales + Creator + Marketing + Events + Digital Commerce + Business Operating Platform**

The platform should allow one person to use one account for multiple roles and businesses.

Possible roles include:

- Student
- Creator
- Freelancer
- Professional
- Marketer
- Consultant
- Tutor
- Business owner
- Seller
- Service provider
- Promoter
- Event organizer
- Digital product creator

### One-account principle

One canonical account per person.

A person can have:

- Personal profile
- Creator profile/page
- Multiple businesses
- Multiple workspaces
- Products
- Services
- Events
- Campaigns
- Websites
- Mini apps
- Content
- Affiliate activity

Avoid forcing the same person to create duplicate accounts for different business activities.

---

## 3. Primary Product Flow

Target post-login flow:

```text
Login / Signup
      ↓
Create Account
      ↓
Define Yourself
(role / goals; optional where appropriate)
      ↓
Business Marketplace
```

Marketplace should become the primary center of the product.

Recommended primary navigation:

```text
Market | Discover | Create | Connect | Profile
```

Calls should live under Connect / CRM, not replace the Marketplace as the primary home.

---

## 4. Marketplace

Marketplace is the primary discovery layer.

It should support:

- Products
- Services
- Businesses
- Jobs
- Property
- Vehicles
- Events
- Digital products
- Knowledge
- New products
- Used products
- Local businesses
- Local services
- Nearby listings
- Online offerings
- Requirements
- Service bidding

### Marketplace discovery

- Search
- Categories
- Subcategories
- Industry
- Requirements
- Location
- City
- Area/radius
- Nearby
- Map
- Filters
- Sort
- Recommended listings
- Verified businesses/sellers
- Listing details
- Contact
- Chat
- Call
- Enquiry
- Share
- QR

### City-first/local discovery

Initial focus can be Pune, Maharashtra, but architecture must support:

- Pune
- Mumbai
- Nagpur
- Nashik
- Other Indian cities
- India-wide discovery

---

## 5. Products

Support:

- Physical products
- Used products
- New products
- Digital products
- PDFs
- Templates
- Courses
- Knowledge products
- Product media
- Pricing
- Inventory later
- Orders
- Payments
- Seller profile
- Business association
- Product QR
- Product link

---

## 6. Services

Support:

- Professional services
- Freelancer services
- Business services
- Consulting
- Training
- Marketing
- Technology
- Local services
- Service packages
- Service location / service area
- Booking
- Enquiry
- Chat
- Call
- Payment

### Service bidding marketplace

Customer side:

```text
Post Requirement
 ↓
Describe work
 ↓
Budget
 ↓
Location
 ↓
Deadline
 ↓
Receive Proposals
 ↓
Compare Providers
 ↓
Shortlist
 ↓
Chat / Call
 ↓
Select Provider
 ↓
Deal
 ↓
Payment
```

Provider side:

```text
Find Requirement
 ↓
Submit Proposal
 ↓
Price
 ↓
Timeline
 ↓
Message
 ↓
Win Work
```

This should support a two-sided service marketplace.

---

## 7. Businesses

Business profiles should support:

- Business identity
- Business verification
- Owner
- Manager
- Editor
- Promoter
- Multiple locations
- Products
- Services
- Events
- Campaigns
- Website
- Landing page
- Mini app
- WhatsApp
- Phone
- Map
- QR
- Reviews later
- Analytics
- Leads
- CRM
- Payments

### Business authorization

A person should be able to:

- Create their business
- Claim an existing business
- Request management access
- Invite owners/managers/editors/promoters
- Verify ownership/authorization through controlled methods

Do not assume everyone managing a business is its legal owner.

---

## 8. Live Events

Live events are a dedicated discovery category.

Event types:

- Concerts
- Business events
- Workshops
- Seminars
- Conferences
- College events
- Sports
- Exhibitions
- Food events
- Festivals
- Networking
- Creator events
- Community events
- Shopping events
- Kids/family events

Event discovery:

```text
Live Now
Today
Tomorrow
This Weekend
This Week
Upcoming
Nearby
```

### City-wise events

Example:

```text
Pune
 ├── Live Now
 ├── Today
 ├── Tomorrow
 ├── Weekend
 ├── Nearby
 └── Popular
```

Event fields:

- Cover
- Name
- Description
- Date
- Start/end time
- Venue
- Organizer
- Map location
- Ticket price
- Capacity
- Registration
- WhatsApp
- Website
- Payment
- QR
- Share

---

## 9. City-wise Campaigns

Campaigns should support geographic targeting.

```text
Campaign
 ↓
City
 ↓
Area / Radius
 ↓
Audience
 ↓
Creators
 ↓
Content
 ↓
Offer
 ↓
Landing Page
 ↓
Leads / Sales
```

Example:

```text
Pune
20 km radius
Weekend offer
Food audience
Creators
WhatsApp
Marketplace
Landing page
QR
```

Campaign analytics:

- Views
- Reach
- Clicks
- Leads
- WhatsApp contacts
- Calls
- Bookings
- Orders
- Revenue
- ROI

---

## 10. Digital Marketing / Ads

Build a first-party local promotion/ads engine before attempting to replace large external ad networks.

Businesses should be able to promote:

- Product
- Service
- Business
- Event
- Campaign
- Marketplace listing
- Content
- Landing page
- Website
- Creator collaboration

Campaign objectives:

- Awareness
- Traffic
- Leads
- WhatsApp
- Calls
- Bookings
- Sales
- Event registration
- Mini-app visits

Campaign controls:

- City
- Area
- Radius
- Audience
- Budget
- Duration
- Objective
- Creative
- Landing page
- Creator
- QR
- Tracking

Dashboard:

```text
Budget
Spent
Reach
Views
Clicks
Leads
WhatsApp
Calls
Bookings
Orders
Revenue
ROI
```

---

## 11. Content Studio

Create:

- Posts
- Images
- Videos
- Reels
- Stories
- Text posts
- Offers
- Product posts
- Service posts
- Event posts
- Campaign posts

### Content scheduling

```text
Create
 ↓
Edit
 ↓
Preview
 ↓
Schedule
 ↓
Publish
```

Content calendar:

- Daily
- Weekly
- Monthly

Potential channels, subject to official API/permission support:

- Marketplace
- Website
- Mini app
- WhatsApp
- Instagram
- Facebook
- YouTube

Do not use unofficial automation methods that put accounts at risk.

---

## 12. WhatsApp Automation

WhatsApp is a major business communication module.

Support, through appropriate official APIs/platforms:

- Business WhatsApp
- Message templates
- Welcome messages
- Lead follow-ups
- Appointment reminders
- Payment reminders
- Order updates
- Campaign messages
- Customer segmentation
- Scheduled messages
- Message history
- Opt-in/opt-out
- Campaign tracking
- Click tracking
- Reply tracking

WhatsApp should connect to:

```text
Marketplace
Listings
Leads
CRM
Bookings
Payments
Campaigns
Creators
Landing pages
QR
```

---

## 13. Calls

Existing call-log functionality is foundational.

Preserve:

- Incoming call metadata
- Outgoing call metadata
- Missed calls
- Call history
- Phone number
- Date/time
- Duration
- Call type
- Contact association
- Lead association
- Call outcome
- Follow-up

### Critical Android limitation

Normal Android `CallLog` metadata does NOT automatically provide cellular call audio.

Do not assume the app can record/transcribe ordinary cellular calls.

If voice transcription is added later, it must use a supported audio source/API and comply with platform/legal requirements.

---

## 14. Meetings + Calendar

Support:

- Schedule meeting
- Date/time
- Meeting type
- Online/offline
- Meeting link
- Attendees
- Agenda
- Reminder
- Reschedule
- Cancel

Unified calendar should combine:

- Calls
- Meetings
- Follow-ups
- Tasks
- Bookings
- Events
- Campaign activities
- Payment deadlines

---

## 15. Notes / Auto Notes

Support:

- Manual notes
- Customer notes
- Business notes
- Meeting notes
- Call outcome
- Next action
- Follow-up date
- Activity timeline

AI can optionally assist later with summaries/notes.

AI must NOT be a dependency for core functionality.

---

## 16. CRM

CRM should connect marketplace activity to sales.

```text
Marketplace
 ↓
Contact
 ↓
Lead
 ↓
Call / Chat / WhatsApp
 ↓
Meeting
 ↓
Follow-up
 ↓
Proposal
 ↓
Deal
 ↓
Order
 ↓
Payment
 ↓
Revenue
```

Support:

- Leads
- Contacts
- Activities
- Calls
- Chats
- Notes
- Follow-ups
- Tasks
- Meetings
- Pipeline
- Opportunities
- Deals
- Orders
- Conversion
- Reporting

---

## 17. Creator Pages

Creators need a dedicated public identity.

Creator page:

- Profile
- Bio
- Category
- Location
- Skills
- Portfolio
- Social links
- Followers later
- Services
- Digital products
- Events
- Campaigns
- Affiliate products
- Affiliate links
- QR
- Contact
- Collaboration option

Example:

```text
Creator
 ↓
Creator Page
 ├── Content
 ├── Portfolio
 ├── Services
 ├── Products
 ├── Campaigns
 ├── Events
 └── Affiliate
```

---

## 18. Creator Affiliate System

Business:

```text
Campaign
 ↓
Select Creator
 ↓
Creator receives unique link / QR
```

Customer:

```text
Creator Link
 ↓
Click
 ↓
Listing / Landing Page
 ↓
Lead / Purchase
```

Track:

- Clicks
- Leads
- Orders
- Revenue
- Conversion
- Commission
- Pending commission
- Paid commission

Business dashboard:

- Creators
- Campaigns
- Clicks
- Leads
- Sales
- Revenue
- Commission
- ROI

Creator dashboard:

- Links
- QR codes
- Clicks
- Leads
- Sales
- Earnings
- Pending
- Paid

---

## 19. QR System

QR should support multiple destinations:

- Personal profile
- Business profile
- Product
- Service
- Event
- Campaign
- Landing page
- Payment
- Affiliate link
- WhatsApp
- Contact
- Mini app
- Website

QR tracking should support:

```text
Scan
 ↓
Destination
 ↓
Click / visit
 ↓
Lead / order / payment
```

---

## 20. Payment Links

Support:

```text
Create Payment Link
 ↓
Amount
 ↓
Product / Service
 ↓
Customer
 ↓
Share
 ↓
Checkout
 ↓
Payment
 ↓
Order / Booking
 ↓
Ledger
```

Share through:

- WhatsApp
- Social channels
- Website
- Landing page
- QR
- Marketplace
- Creator affiliate links
- Email/SMS where appropriate

---

## 21. Payment Gateway

Architecture must separate:

- Payment link
- Checkout
- Gateway
- Transaction
- Order
- Seller balance/ledger
- Platform fees
- Refunds

Initially integrate established compliant payment providers rather than becoming a payment processor.

Do not store payment secrets/API keys in the APK.

---

## 22. Maps / Location

Maps should be platform-wide.

Support:

- User location
- Business location
- Multiple business locations
- Marketplace nearby
- Events nearby
- Service area
- Campaign radius
- Map search
- Directions
- Distance
- Local discovery

---

## 23. Website Builder

No-code/simple builder for nontechnical users.

```text
Choose Template
 ↓
Customize
 ↓
Add Business
 ↓
Add Products/Services
 ↓
Add Contact/Map/Booking/Payment
 ↓
Preview
 ↓
Publish
```

Website components:

- Hero
- About
- Products
- Services
- Events
- Offers
- Gallery
- Testimonials
- FAQ
- Contact
- WhatsApp
- Map
- Booking
- Payment
- QR

AI can later help generate content, but template/manual creation must work without AI.

---

## 24. Landing Page Builder

Separate lightweight landing-page experience.

```text
Campaign
 ↓
Landing Page
 ↓
Offer
 ↓
CTA
 ↓
Lead form
 ↓
CRM
```

Support:

- Hero
- Offer
- Product/service
- Benefits
- Images/video
- Pricing
- Testimonials
- FAQ
- Lead form
- WhatsApp
- Call
- Payment link
- Map
- CTA
- Analytics/tracking

---

## 25. Mini Apps

Businesses/creators should eventually create mini apps without coding.

Example restaurant:

```text
Home
Menu
Order
Booking
Offers
Location
WhatsApp
Payment
```

Tutor:

```text
Courses
Classes
Schedule
Booking
Payment
WhatsApp
```

Freelancer:

```text
Portfolio
Services
Packages
Booking
Payment
Contact
```

Mini apps should reuse the same account, business, listing, payment and CRM infrastructure.

---

## 26. Analytics / Growth Loop

Everything should connect into one measurable funnel:

```text
Impression
 ↓
View
 ↓
Click
 ↓
Visit
 ↓
WhatsApp / Call / Chat
 ↓
Lead
 ↓
Meeting
 ↓
Proposal
 ↓
Booking / Order
 ↓
Payment
 ↓
Revenue
```

Analytics should eventually show:

- Views
- Reach
- Clicks
- Leads
- Calls
- Chats
- Meetings
- Bookings
- Orders
- Payments
- Revenue
- Conversion rate
- Campaign ROI
- Creator performance
- Affiliate performance

---

## 27. Monetization Plan

The platform itself should have multiple revenue streams.

### Free

- Account
- Basic profile
- Basic marketplace browsing
- Limited listings
- Basic creator page

### Pro

- More listings
- Website
- Landing pages
- Content scheduling
- Analytics
- CRM
- Automation

### Business

- Multiple employees
- Multiple businesses/workspaces
- Advanced CRM
- Campaigns
- WhatsApp integration
- Advanced analytics
- Mini apps

### Platform revenue possibilities

- Subscriptions
- Promoted listings
- Advertising
- Campaign services
- Marketplace service fees
- Booking/service fees
- Digital-product transaction fees
- Creator marketplace services
- Optional premium tools

Actual payment/marketplace fees must be designed for applicable Indian laws, provider terms and tax requirements.

---

## 28. Roles and Permissions

Business/workspace roles:

- Owner
- Manager
- Editor
- Salesperson
- Promoter
- Creator/collaborator
- Viewer

Permissions should control:

- Listings
- Products
- Services
- Leads
- CRM
- Campaigns
- Content
- Payments
- Analytics
- Business profile
- Website
- Mini app

---

## 29. Identity / KYC Principle

Do not require KYC for every basic user.

Basic account can work without KYC.

KYC/verification should be introduced where appropriate, especially:

- Earning money
- Seller payouts
- Financial transactions
- Business verification
- Creator monetization
- Marketplace trust features

Do not store Aadhaar/PAN as the primary identity in Room.

---

## 30. Technical Architecture

Initial architecture:

**Modular monolith**, not microservices.

Android:

- Kotlin
- Jetpack Compose
- Room
- Android platform APIs

Backend later:

- REST/API-first architecture
- PostgreSQL
- Object/blob storage such as Azure Blob
- Authentication
- Authorization
- Webhooks
- External integrations

Principles:

- Offline/local cache where useful
- Deterministic workflows
- API-first
- AI optional/later
- No secrets/API keys in APK
- Clear module boundaries
- Testable repositories/ViewModels
- Preserve backward compatibility and migrations

---

## 31. Current Android Database Foundation

Current Room database version: **10**

Current entities include:

- PersonEntity
- ActivityEntity
- ConversationEntity
- ConversationMemberEntity
- ChatMessageEntity
- BusinessProfileEntity
- OfferingEntity
- MediaAttachmentEntity
- UserAccountEntity
- UserIdentityEntity
- UserProfileEntity
- UserProfessionEntity
- CategoryEntity
- IndustryEntity
- RequirementEntity
- MarketplaceListingEntity

Current marketplace tables:

- categories
- industries
- requirements
- marketplace_listings

Current marketplace layers:

```text
MarketplaceListingEntity
 ↓
MarketplaceListingDao
 ↓
MarketplaceListingRepository
 ↓
MarketplaceListingViewModel
 ↓
MarketplaceScreen
```

---

## 32. Current Marketplace Listing Model

Current listing supports:

- id
- ownerId
- workspaceId
- title
- description
- type
- categoryId
- industryId
- requirementId
- price
- currency
- condition
- status
- latitude
- longitude
- address
- contactPhone
- websiteUrl
- imageUri
- isVerified
- isActive
- createdAt
- updatedAt

Types already considered:

- PRODUCT
- SERVICE
- BUSINESS
- JOB
- PROPERTY
- VEHICLE
- EVENT
- DIGITAL_PRODUCT
- KNOWLEDGE

---

## 33. Current Navigation Concept

Routes currently include concepts for:

- Main
- Discover
- Create
- Connect
- Chats
- Profile
- People
- Calls
- More
- Earn
- BusinessProfile
- Marketplace
- Campaigns
- Services
- Products
- Website
- Locations
- EventSearch
- CreateOffering
- ChatConversation
- CallDetails

Do not create duplicate routes/modules without checking existing navigation.

---

## 34. Current Create Flow

Current Create options include:

- BUSINESS
- PRODUCT
- SERVICE
- KNOWLEDGE
- FRANCHISE
- CAMPAIGN
- EVENT
- WEBSITE
- MINI_APP
- CHANNEL
- CONTENT

Business currently goes to Business Profile.

Other offering types currently go through the existing creation screen.

---

## 35. Existing OfferingCreateScreen

The active creation screen is:

`ui/create/OfferingCreateScreen.kt`

There is also:

`ui/platform/OfferingCreateScreen.kt`

The `ui/platform` copy is currently blank and is NOT the active route.

Navigation imports:

`com.example.salescalltracker.ui.create.OfferingCreateScreen`

The active creation screen already contains:

- Title
- Description
- Price
- Website/portfolio/demo link
- Photo picker
- Video picker
- Animation/GIF picker
- PDF/document picker
- Added media/details display
- Save Draft
- Back

Do not rebuild this screen unnecessarily.

---

## 36. Current Working Checkpoint

The project has successfully compiled after the Marketplace UI work.

Commands confirmed working:

```powershell
.\gradlew.bat compileDebugKotlin
```

and previously:

```powershell
.\gradlew.bat test
```

Gradle 9.1.0
Kotlin 2.2.0
Android Studio JBR/JDK 25.0.2

Correct JAVA_HOME root:

`C:\Program Files\Android\Android Studio\jbr`

JDK 25 restricted `java.lang.System::load` warnings from Gradle/native-platform are currently harmless when builds succeed.

---

## 37. Call-log Preservation Rule

This is a hard rule:

**Never break the existing working call-log reader while adding marketplace/business features.**

The call-log reader uses Android `CallLog.Calls.CONTENT_URI` and reads metadata such as:

- number
- type
- date
- duration

The current design loads recent call records.

Any future CRM integration should wrap/consume this functionality rather than replace it.

---

## 38. Product Development Strategy

Do not implement every feature simultaneously.

Recommended order:

### Phase 1 — Foundation
- Account
- Profile
- Room
- Marketplace foundation
- Existing call logs

### Phase 2 — Marketplace Core
- Professional marketplace UI
- Search
- Categories
- Filters
- Location
- Listing cards
- Listing details

### Phase 3 — Create & Sell
- Product
- Service
- Business
- Event
- Listing creation
- Media
- Categories/industry/requirements

### Phase 4 — Location
- Map
- Nearby
- Business locations
- Event locations
- Service areas

### Phase 5 — Communication
- Chat
- Calls
- WhatsApp
- Meeting
- Calendar
- Reminders

### Phase 6 — CRM
- Leads
- Notes
- Auto/manual notes
- Follow-ups
- Pipeline
- Activities
- Reporting

### Phase 7 — Service Marketplace
- Requirements
- Bidding
- Proposals
- Provider comparison
- Deal selection

### Phase 8 — Payments
- Orders
- Bookings
- Payment links
- Checkout
- Gateway integration
- Transaction/ledger

### Phase 9 — Growth
- Campaigns
- City targeting
- Ads/promotions
- Landing pages
- QR
- Analytics

### Phase 10 — Creator Economy
- Creator pages
- Creator campaigns
- Affiliate links
- Affiliate QR
- Commission
- Earnings

### Phase 11 — Content
- Post creation
- Content studio
- Scheduling
- Calendar
- Publishing integrations

### Phase 12 — Web
- Website templates
- Website builder
- Landing-page builder

### Phase 13 — Mini Apps
- Mini-app templates
- Business mini apps
- Creator mini apps
- Booking/order/payment integration

---

## 39. Non-negotiable Product Principles

1. **One account, many roles.**
2. **Marketplace is the primary discovery center.**
3. **CRM is connected to marketplace activity.**
4. **Calls remain a foundational capability.**
5. **AI is optional, not a dependency.**
6. **Use official APIs for external platform integrations.**
7. **Do not put secrets/API keys in the Android APK.**
8. **Use deterministic automation wherever possible.**
9. **Do not require KYC for basic accounts.**
10. **Do not store Aadhaar/PAN as primary identity in Room.**
11. **Use modular architecture rather than premature microservices.**
12. **Preserve working functionality when adding new modules.**
13. **Prefer reusable platform primitives over duplicated features.**
14. **Build for Indian users first, with city/local discovery as a major advantage.**
15. **Do not silently drop previously agreed features from the roadmap.**
16. **When a new requirement is mentioned, add it to this project memory before treating the roadmap as complete.**

---

## 40. How This File Should Be Used

Before making significant project changes:

1. Read this file.
2. Identify existing functionality affected.
3. Identify which module the new feature belongs to.
4. Reuse existing entities/repositories/navigation where appropriate.
5. Avoid duplicate screens/models/services.
6. Make small changes.
7. Compile/test.
8. Update this file when a new durable product requirement is agreed.
9. Never intentionally remove a feature from this document without explicit approval.

### Current immediate implementation goal

The next practical goal is:

```text
Create
 ↓
Product / Service / Business / Event
 ↓
Existing creation screen
 ↓
Save
 ↓
MarketplaceListingEntity
 ↓
Room
 ↓
Marketplace
 ↓
Search
 ↓
Filter
 ↓
Listing Details
```

After that:

```text
Listing
 ↓
Chat / Call / WhatsApp / Meeting
 ↓
Lead
 ↓
CRM
 ↓
Booking / Order
 ↓
Payment
```

---

## 41. Feature Inventory Checklist

Use this checklist when reviewing the product so features are not forgotten:

### Identity
- [ ] One account
- [ ] Personal profile
- [ ] Multiple businesses/workspaces
- [ ] Roles/permissions
- [ ] Verification/KYC where appropriate

### Marketplace
- [ ] Products
- [ ] Services
- [ ] Businesses
- [ ] Jobs
- [ ] Property
- [ ] Vehicles
- [ ] Events
- [ ] Digital products
- [ ] Knowledge
- [ ] Search
- [ ] Categories
- [ ] Location
- [ ] Map
- [ ] Nearby
- [ ] Listing details

### Services
- [ ] Requirements
- [ ] Bidding
- [ ] Proposals
- [ ] Provider comparison
- [ ] Booking

### Events
- [ ] Live now
- [ ] Today
- [ ] Weekend
- [ ] City-wise
- [ ] Nearby
- [ ] Tickets
- [ ] Registration
- [ ] Event QR

### Marketing
- [ ] Campaigns
- [ ] City targeting
- [ ] Radius targeting
- [ ] Digital ads
- [ ] Promoted listings
- [ ] Landing pages
- [ ] QR tracking
- [ ] Analytics

### Content
- [ ] Post creation
- [ ] Image
- [Video
- [ ] Reel
- [ ] Story
- [ ] Content calendar
- [ ] Scheduling
- [ ] Publishing integrations

### Creator
- [ ] Creator page
- [ ] Portfolio
- [ ] Services
- [ ] Digital products
- [ ] Campaigns
- [ ] Affiliate links
- [ ] Affiliate QR
- [ ] Commission
- [ ] Earnings

### Communication
- [ ] Chat
- [ ] Calls
- [ ] Call log
- [ ] WhatsApp
- [ ] WhatsApp templates
- [ ] WhatsApp automation
- [ ] Meeting
- [ ] Calendar
- [ ] Reminders
- [ ] Notes
- [ ] Follow-ups

### CRM
- [ ] Leads
- [ ] Contacts
- [ ] Activities
- [ ] Pipeline
- [ ] Proposals
- [ ] Deals
- [ ] Conversion
- [ ] Reports

### Commerce
- [ ] Orders
- [ ] Bookings
- [ ] Payment links
- [ ] Checkout
- [ ] Gateway
- [ ] Transactions
- [ ] Ledger
- [ ] Refunds

### Web / App Builder
- [ ] Website templates
- [ ] Website builder
- [ ] Landing pages
- [ ] Mini apps
- [ ] Mini-app templates
- [ ] Business mini apps

---

## 42. Change Log

### 2026-09-05
- Confirmed Marketplace as the main product center.
- Confirmed Create → listing → Room → Marketplace pipeline.
- Expanded master requirements to include:
  - Live events
  - City-wise campaigns
  - Digital marketing/ads
  - Post creation and scheduling
  - Creator pages
  - Creator monetization
  - WhatsApp automation/messages
  - Meetings/calls/calendar scheduling
  - Notes/auto-notes
  - Mini apps
  - Website templates
  - Landing pages
  - Creator affiliate links
  - QR system
  - Payment links
  - Payment gateway
  - Map/location
  - Service bidding
- Existing call-log functionality remains protected.

## 43. Context-Adaptive Super-Platform Experience

### Product Principle
The platform must feel like a combination of multiple best-in-class digital products while remaining **one unified application, one account, and one ecosystem**. It should not feel like a simple classifieds/listing marketplace.

The key principle is:

> **One account + one platform, but the experience adapts to what the user is trying to do.**

The platform should not copy the branding, proprietary UI, code, or exact design of other products. Instead, it should provide its own interface using familiar, high-quality workflow patterns appropriate to each category.

### Context / Intent-Based Experience
Introduce a conceptual **Marketplace Intent / Experience Engine** that detects or receives the user's intent/category and changes the experience accordingly.

Core intents include:
- FOOD
- TRAVEL
- SHOPPING
- SERVICES
- SOCIAL
- CREATOR
- BUSINESS
- EVENT
- PROPERTY
- VEHICLE
- JOBS
- B2B

Examples:
- Food/restaurant search → food discovery, restaurant pages, menus, cart, order and delivery-style workflow.
- Cab/travel search → pickup/destination, cab options, booking, transport and trip workflow.
- Bus/travel search → route search, schedules, seat selection and booking workflow.
- Tour search → destinations, packages, availability and booking workflow.
- Ecommerce search → products, categories, deals, product details, cart, checkout and orders.
- Creator search → creator feed, profiles, posts/reels/live, follow, message, creator services, products and affiliate opportunities.
- Map/location search → map-first discovery of restaurants, businesses, services, events and nearby listings.
- Communication → unified chat, groups, files, locations, listings, quotations, payment links, meetings, calls and related business actions.
- Services → requirements, providers, proposals/bidding, chat, calls, meetings, agreements, payments and completion.
- Events → event discovery, live events, location, registration/booking and related communication.
- Business → business profile, products, services, leads, CRM, campaigns, advertising, content, calendar, team and analytics.

### Marketplace as the Front Door
Marketplace is the primary discovery center, but it should evolve beyond a listing feed into a full discovery/transaction layer:

```text
Marketplace
├── Search
├── Location / Nearby
├── Categories
├── Products
├── Services
├── Businesses
├── Events
├── Jobs
├── Property
├── Vehicles
├── Digital Products
├── Live Now
└── Campaigns
```

The same platform infrastructure should route users into specialized experiences without requiring separate applications or separate accounts.

### Unified Journey
A major product journey is:

```text
Search / Discovery
    ↓
Intent / Category
    ↓
Specialized Experience
    ↓
Listing / Business / Creator / Service
    ↓
Location + Details
    ↓
Chat | Call | WhatsApp | Book | Buy | Enquire
    ↓
Lead / Order / Booking / Proposal
    ↓
Calendar / Meeting / CRM
    ↓
Payment / Completion
    ↓
Review / Repeat / Follow-up
```

### Platform Modules
The long-term platform should combine these experiences as modules under one app shell:

```text
APP SHELL
├── Identity / One Account
├── Marketplace / Discovery
│   ├── Food
│   ├── Travel
│   ├── Shopping
│   ├── Services
│   ├── Events
│   ├── Businesses
│   ├── Jobs
│   ├── Property
│   ├── Vehicles
│   ├── Creators
│   └── B2B
├── Social / Creator
├── Maps / Location
├── Communication
├── CRM
├── Marketing / Campaigns / Ads
├── Commerce / Bookings / Orders
├── Payments
├── Affiliate / QR / Commission
├── Calendar / Meetings / Calls / Notes
├── Website / Landing Page Builder
└── Mini Apps
```

### Important UX Requirement
The application may be technically modular, but the user experience should feel cohesive. Do not expose unnecessary technical complexity to the user. Specialized modes should appear naturally from search, category selection, location, or user intent.

### Examples of Desired Product Feel
The target is a familiar level of depth and workflow quality in each context:
- Food: modern food discovery/order workflow
- Travel/cab: modern ride/travel booking workflow
- Bus: modern route/seat booking workflow
- Ecommerce: modern catalog/cart/checkout workflow
- Creator/social: modern feed/profile/follow/content workflow
- Maps: modern map/search/nearby workflow
- Communication: modern chat/group/call workflow
- Services: modern requirement/proposal/bidding workflow
- Business: modern business dashboard/CRM/marketing workflow

These are **experience references, not dependencies or copies**. The product must maintain its own identity and architecture.

### Engineering Principle
Do not create separate standalone apps inside the APK. Build one modular platform with shared infrastructure:
- One identity/account system
- Shared profiles and workspaces
- Shared marketplace/listing model
- Shared location system
- Shared communication system
- Shared transaction/payment model
- Shared notification system
- Shared analytics
- Shared CRM
- Intent-specific presentation and workflows

The existing Android call-log functionality is a protected foundation and must continue working while these experiences are added around it.

### Development Strategy
Build progressively rather than implementing every vertical simultaneously:
1. Marketplace shell + search + categories + location + intent navigation
2. Specialized Food, Travel, Shopping, Services and Events experiences
3. Creator/Social experience
4. Communication: chat, groups, calls, meetings, calendar and notes
5. Business: CRM, campaigns, digital marketing and ads
6. Payments: links, QR, checkout, gateway, transactions and commissions
7. Website templates, landing pages and mini-app builder

---

### 2026-09-05 — Added Super-Platform Experience Direction
- Added the requirement that the app feel like a unified combination of marketplace, food, travel, ecommerce, creator/social, maps, communication, services, events, business and commerce platforms.
- Added Context-Adaptive / Intent-Based Experience as a core product principle.
- Added Marketplace Intent / Experience Engine concept.
- Defined specialized workflows for food, cab/travel, bus, tours, ecommerce, creators, maps, communication, services, events and business.
- Confirmed that specialized experiences must be implemented as modules within one application rather than separate apps.
- Confirmed that familiar platform patterns may guide UX quality, but proprietary branding, exact UI and code must not be copied.
- Confirmed Marketplace remains the front door and discovery center.

## 44. Location & Local Discovery Engine

### Product Principle
Location is a core platform capability, not merely a standalone Locations screen. The platform should use location to improve local discovery, search relevance, maps, service-area matching and context-specific experiences.

### Location Capabilities
The Location Engine should provide:
- Current location
- Selected/manual location
- Location permission state
- Search radius
- Nearby search
- Latitude/longitude
- Address
- City/state/pincode where appropriate
- Geocoding and reverse geocoding
- Business/service area
- Location visibility/privacy controls
- Location history only where there is a clear user benefit and appropriate consent

### Permission Strategy
Start with foreground / "while using the app" location access for nearby businesses, local services, products, events, restaurants, travel and map-first discovery. Do not make continuous/background location tracking a default requirement.

### Location-Aware Marketplace
Marketplace search should combine platform-owned listings with nearby businesses, products, services and events, plus external place/provider data where legally and technically permitted.

Do not scrape or bulk-import third-party business databases without authorization. Use supported APIs, user authorization/ownership flows or properly licensed data.

### Existing Data Foundation
The marketplace listing model already supports latitude, longitude and address, so location-aware search can be added incrementally.

## 45. Profile Import & Digital Presence Engine

### Product Principle
Users should be able to connect authorized external accounts and use permitted profile/business information as a starting template for their unified platform identity.

Potential integrations:
- Google
- Instagram
- Facebook
- WhatsApp
- Telegram
- LinkedIn

Integrations are optional. A basic platform profile must work without connecting every external account.

### Account Connection Flow
```text
Create account
    ↓
Complete digital profile
    ↓
Choose external accounts to connect
    ↓
OAuth / supported authorization
    ↓
Import permitted information
    ↓
User reviews and confirms fields
    ↓
Create / update platform profile
```

Possible imported information, subject to provider permissions and policies:
- Name
- Profile photo
- Bio/about
- Business name
- Category
- Public links
- Contact options
- Location
- Services/products where available
- Working hours
- Other provider-approved profile data

Never request or store third-party passwords.

### Profile Types
One account can support:
- Personal profile
- Creator profile
- Freelancer/professional profile
- Business profile
- Seller profile
- Consultant profile
- Promoter/affiliate profile

### Digital Presence Generation
Approved profile data can be used as a starting template for:
- Public profile
- Business profile
- Website
- Landing page
- Storefront
- Creator page
- Mini app
- Shareable profile page

Generated assets must remain editable.

### Business Import / Claim
Distinguish between:
- User-owned/authorized business
- Business the user is authorized to manage
- External business discovered for local search

Only authorized business-management data should become a managed platform business profile. Use ownership/claim/verification flows where required.

### Affiliate, Referral & QR Layer
Eligible users can add:
- Affiliate links
- Referral links
- QR codes
- Payment links
- Product/service links
- Booking links
- Promotional links

Measurable routing:
```text
QR
 ↓
Promoter / Affiliate ID
 ↓
Profile / Landing Page
 ↓
Product / Service
 ↓
Click
 ↓
Lead / Order / Booking
 ↓
Commission
```

Track where applicable:
- Views
- Clicks
- Leads
- Orders
- Bookings
- Conversions
- Commission
- Payout/ledger status

Affiliate/referral rewards must be tied to legitimate transactions or defined conversion events, not recruitment-chain economics.

### External Account Security
Use:
- OAuth/provider-supported authorization
- Explicit consent
- Minimum required scopes
- Secure server-side token handling when persistent access is needed
- No API secrets embedded in the APK
- Disconnect/revoke controls
- Import review before publishing
- Provider-specific API limits and policies

The core Android app must remain functional when an external provider is not connected or unavailable.

### 2026-09-05 — Added Location, Profile Import & Digital Presence Architecture
- Added Location & Local Discovery Engine as a core platform capability.
- Added foreground/while-using location as the initial implementation strategy.
- Added location-aware marketplace search, radius filtering, map readiness and nearby discovery.
- Added requirement to use authorized APIs/licensed providers rather than scraping third-party business databases.
- Added Profile Import & Digital Presence Engine.
- Added optional authorized connections for Google, Instagram, Facebook, WhatsApp, Telegram and LinkedIn.
- Added user review/confirmation before imported profile information is published.
- Added conversion of one unified profile into public profile, business profile, website, landing page, storefront, creator page and mini app.
- Added affiliate/referral links, QR codes, payment links and measurable conversion/commission tracking.
- Added secure OAuth/permission/revocation principles and prohibition on storing third-party passwords.
- Added business ownership/authorization/claim distinction for imported business profiles.
