import React, { Component } from "react";
import { HashRouter, Route, Switch } from "react-router-dom";
import { TheLayout } from "./layout";
import "./scss/style.scss";
import Login from "./views/dashboard/login/Login";

const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
);

class App extends Component {
  state = {
    isLogged: false,
  };

  setUserSession = (userSession) => {
    localStorage.setItem("userName", userSession.user.userName);
    localStorage.setItem("roleId", userSession.user.role.id);
    localStorage.setItem("sessionId", userSession.sessionId);
    this.setState({
      isLogged: true,
    });
  };

  render() {
    if (!localStorage.getItem("sessionId"))
      return <Login setUserSession={this.setUserSession}></Login>;
    else
      return (
        <HashRouter>
          <Switch>
            <Route
              path="/"
              name="Home"
              render={(props) => <TheLayout {...props} />}
            />
          </Switch>
        </HashRouter>
      );
  }
}

export default App;
