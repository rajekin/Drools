<!DOCTYPE html>
<html>
<head>
  <title>IBM ODM Quick Login Dashboard</title>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; background: #f4f4f4; }
    .card { background: white; padding: 20px; margin: 10px 0; border-radius: 8px; box-shadow: 0 0 5px rgba(0,0,0,0.1); }
    button { padding: 10px 20px; font-size: 16px; cursor: pointer; }
  </style>
</head>
<body>
  <h2>IBM ODM Quick Login Dashboard</h2>

  <div class="card">
    <h3>Login to Decision Center</h3>
    <button onclick="loginToODM('https://your-odm-server/decisioncenter/j_security_check')">Login</button>
  </div>

  <div class="card">
    <h3>Login to Rule Execution Server</h3>
    <button onclick="loginToODM('https://your-odm-server/res/j_security_check')">Login</button>
  </div>

  <script>
    function loginToODM(url) {
      const form = document.createElement('form');
      form.method = 'POST';
      form.action = url;
      form.target = '_blank';

      const usernameInput = document.createElement('input');
      usernameInput.type = 'hidden';
      usernameInput.name = 'j_username';
      usernameInput.value = 'your_username';

      const passwordInput = document.createElement('input');
      passwordInput.type = 'hidden';
      passwordInput.name = 'j_password';
      passwordInput.value = 'your_password';

      form.appendChild(usernameInput);
      form.appendChild(passwordInput);
      document.body.appendChild(form);
      form.submit();
      form.remove();
    }
  </script>
</body>
</html>
