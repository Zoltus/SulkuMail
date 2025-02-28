SulkuMail is a modern, cross-platform email client built with Kotlin Multiplatform and Compose, providing a consistent experience across Android, iOS, Desktop, and Server platforms.


🛠Technology Stack
- Kotlin Multiplatform - Share code between platforms
- Kotlin Compose - Modern UI toolkit for all platforms
- Ktor - HTTP client for network requests
- Koin - Dependency injection
- Google Credentials Manager & Identity service - Native Android & IOS login
- Multiplatform Settings - Secure credential storage
  
Roadmap
- Complete account authentication flows on ios
- Implement secure credential storage
- Add proper mail viewing capabilities
- Add support for multiple accounts
- Implement local caching for offline access
- Background synchronization
- Rich text composition

## Images
<div style="display: flex; gap: 20px;">
  <img src="https://github.com/user-attachments/assets/d96f0d96-fdda-4be1-b84f-634ed49dde1e" width="48%">
  <img src="https://github.com/user-attachments/assets/48930803-dc04-4b35-af3d-4014bf593795" width="20%" height="20%">
</div>



## Currently under work
**Account Management (Google)**
  - [x] Native Android Login
  - [x] Desktop Login
  - [ ] Native IOS Login
  - [ ] Get account details, name,email & profile picture

**Security & Storage**
  - [ ] Android (DataStoreSettings)
  - [ ] Desktop (DataStoreSettings)
  - [ ] IOS (KeyChain)

**Mail Functionality**
  - [X] Fetching mail logic
  - [X] Delete mail Logic
  - [ ] Batch delete mails
  - [ ] Mail paginator/infinity scrolling
  - [ ] Background tasks for fetching mails
  - [ ] Save fetched mails

**User Interface**
  - [X] Siderbar containing users mail folders, Manage Accounts & Settings at the bottom
  - [X] Basic view to see snippets of fetched mails
  - [ ] Complete mail content view with attachments
  - [ ] Display Users mail folders & info on sidebar
  - [ ] Settings UI
  - [ ] ManageAccounts UI (View & manage added Mail accounts)
- [ ] Robust error handling
- [ ] Handle multiple gmail accounts

## Project Setup
To setup the project do the following steps:

1. Clone the repository
     ```sh
    git clone https://github.com/your-username/weatherapp.git
   ```

2. Open project in IntelliJ
3. Create local.properties in project root directory with:
     ```sh
    sdk.dir=/path/to/your/android/sdk
    GOOGLE_API_SECRET=your_google_client_secret
    GOOGLE_CLIENT_ID=your_google_client_id
    GOOGLE_REDIRECT_URL=http://localhost:8079/callback // This is callback url for the jvm, jvm hosts own http server to receive google auth code, no need to change
    BACKEND_URL=your_backend_url
   ```

