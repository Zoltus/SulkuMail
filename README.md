SulkuMail is a modern, cross-platform email client built with Kotlin Multiplatform and Compose, providing a consistent experience across Android, iOS, Desktop, and Server platforms.
Project is on very early stages and at first the focus is adding full gmail support to read and delete emails, and later adding other mail providers + sending mails.

🛠Technology Stack
- Kotlin Multiplatform - Share code between platforms
- Kotlin Compose - Modern UI toolkit for all platforms
- Koog - To build ai agents to summarize & generate mails 
- Ktor - HTTP client for network requests
- Koin - Dependency injection
- Google Credentials Manager & Identity service - Native Android & IOS login
- Multiplatform Settings - Secure credential storage

Roadmap
- Add ai agent to summarize & generate mails
- Add proper support for multiple accounts
- Add proper mail viewing capabilities
- Implement secure credential storage
- Implement local caching for offline access
- Background synchronization
- Complete account authentication flows on ios
- Support multiple llm models for ai agent (e.g. OpenAI, Gemini)

## Images
<div style="display: flex; gap: 20px;">

   <img src="https://github.com/user-attachments/assets/f86cb548-6dd3-4d24-b117-159e74db3d45" width="20%">
   <img src="https://github.com/user-attachments/assets/a144c132-fc21-4dae-a560-f7ec74bd9347" width="20%">
   <img src="https://github.com/user-attachments/assets/3d0c0951-7963-44fd-a7b0-3e22259236d6" width="20%">
   <img src="https://github.com/user-attachments/assets/caec22b1-4fc6-48ef-a015-a4d9c13ef904" width="61%">
   <img src="https://github.com/user-attachments/assets/89965864-36b9-43c4-a267-e1b5a339e7b5" width="61%">
</div>

## Currently under work
**Account Management (Google)**
  - [x] Native Android Login
  - [x] Desktop Login
  - [ ] Native IOS Login
  - [x] Get account details, name,email & profile picture

**Security & Storage**
  - [ ] Android (DataStoreSettings)
  - [ ] Desktop (DataStoreSettings)
  - [ ] IOS (KeyChain)

**Mail Functionality**
  - [X] Fetching mail logic
  - [X] Display html mails.
  - [X] Delete mail Logic
  - [X] Ai agent to generate mails
  - [ ] Ai agent to summarize
  - [ ] Batch delete mails
  - [ ] Mail paginator/infinity scrolling
  - [ ] Background tasks for fetching mails
  - [X] Store mails

**User Interface**
  - [X] Siderbar containing users mail folders, Manage Accounts & Settings at the bottom
  - [X] Basic view to see snippets of fetched mails
  - [ ] Complete mail content view with attachments
  - [X] Display Users mail folders & info on sidebar
  - [ ] Settings UI
  - [ ] ManageAccounts UI (View & manage added Mail accounts)
- [ ] Robust error handling
- [X] Add multiple gmail accounts

## Project Setup
To setup the project do the following steps:

1. Clone the repository
     ```sh
    git clone https://github.com/Zoltus/SulkuMail
   ```

2. Open project in IntelliJ
3. Create local.properties in project root directory with:
     ```sh
    sdk.dir=/path/to/your/android/sdk
    GOOGLE_API_SECRET=your_google_client_secret
    GOOGLE_CLIENT_ID=your_google_client_id
    GOOGLE_REDIRECT_URL=http://localhost:8079/callback // This is callback url for the jvm, jvm hosts own http server to receive google auth code, no need to change
    BACKEND_URL=your_backend_url
    AI_AGENT_URL=ollama_ai_agent_url
   ```

