<!DOCTYPE html>
<html>
<head>
  <title>IBM ODM Login Dashboard</title>
  <style>
    body { font-family: Arial, sans-serif; background: #f2f2f2; padding: 30px; }
    .card { background: white; padding: 25px; max-width: 400px; margin: auto; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    input[type="text"], input[type="password"] {
      width: 100%; padding: 10px; margin: 10px 0; box-sizing: border-box;
    }
    button { padding: 10px 20px; margin-top: 15px; font-size: 16px; cursor: pointer; }
    label { font-weight: bold; }
  </style>
</head>
<body>
  <div class="card">
    <h2>🔐 Login to IBM Decision Center</h2>
    <label for="username">Username</label>
    <input type="text" id="username" placeholder="Enter your username" />

    <label for="password">Password</label>
    <input type="password" id="password" placeholder="Enter your password" />

    <label>
      <input type="checkbox" id="rememberMe"> Remember Me
    </label><br/>

    <button onclick="loginToODM()">Login</button>
  </div>

  <script>
    const loginUrl = "https://review-bk-deposits-dc-811.test-bank.topc4sat.usaa.com/decisioncenter/j_security_check";

    window.onload = function() {
      // Autofill if stored
      if (localStorage.getItem("odm_username")) {
        document.getElementById("username").value = localStorage.getItem("odm_username");
        document.getElementById("password").value = atob(localStorage.getItem("odm_password"));
        document.getElementById("rememberMe").checked = true;
      }
    }

    function loginToODM() {
      const username = document.getElementById("username").value;
      const password = document.getElementById("password").value;
      const remember = document.getElementById("rememberMe").checked;

      if (!username || !password) {
        alert("Please enter both username and password.");
        return;
      }

      if (remember) {
        localStorage.setItem("odm_username", username);
        localStorage.setItem("odm_password", btoa(password)); // base64 encode (not secure, but simple)
      } else {
        localStorage.removeItem("odm_username");
        localStorage.removeItem("odm_password");
      }

      const form = document.createElement("form");
      form.method = "POST";
      form.action = loginUrl;
      form.target = "_blank";

      const userField = document.createElement("input");
      userField.type = "hidden";
      userField.name = "j_username";
      userField.value = username;

      const passField = document.createElement("input");
      passField.type = "hidden";
      passField.name = "j_password";
      passField.value = password;

      form.appendChild(userField);
      form.appendChild(passField);
      document.body.appendChild(form);
      form.submit();
      document.body.removeChild(form);
    }
  </script>
</body>
</html>
