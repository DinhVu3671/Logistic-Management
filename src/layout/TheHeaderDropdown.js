import React, { Component } from 'react'
import {
  CAlert,
  CBadge,
  CDropdown,
  CDropdownItem,
  CDropdownMenu,
  CDropdownToggle,
  CImg
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import userService from '../service/UserService';

class TheHeaderDropdown extends Component {

  state = {
    user: null
  }

  componentDidMount() {
    const sessionId = localStorage.getItem('sessionId');
    const history = window.history;
    userService.getUserSession({ sessionId: sessionId }).then(response => {
      const userSession = response.data.data;
      if (userSession == null) {
        localStorage.clear();
        history.pushState(null, '', '/#');
        window.location.reload();
      }
      this.setState({
        user: userSession.user
      });
      console.log(this.state.user)
    }).catch(e => {
      console.log(e);
    });
  }

  handleLogout = () => {
    console.log("history:", window.history);
    const history = window.history;
    const sessionId = localStorage.getItem('sessionId');
    userService.logout({ sessionId: sessionId }).then(response => {
      if (response.data.code === "SUCCESS") {
        localStorage.clear();
        history.pushState(null, '', '/#');
        window.location.reload();
      }
    });
  }

  render() {
    return (
      <CDropdown
        inNav
        className="c-header-nav-items mx-2"
        direction="down"
      >
        <CDropdownToggle className="c-header-nav-link" caret={false}>
          <div className="c-avatar">
            {
              !this.state.user ?
                <CImg
                  src={'avatars/6.jpg'}
                  className="c-avatar-img"
                  alt="admin@bootstrapmaster.com"
                />
                :
                <CImg
                  src={this.state.user.avatar}
                  className="c-avatar-img"
                  alt={this.state.user.avatar}
                />
            }
          </div>
        </CDropdownToggle>
        <CDropdownMenu className="pt-0" placement="bottom-end">
          <CDropdownItem
            header
            tag="div"
            color="light"
            className="text-center"
          >
            <strong>Account</strong>
          </CDropdownItem>
          <CDropdownItem>
            <CIcon name="cil-user" className="mfe-2" />
            Name:
            {!this.state.user ? "Full name" : " " + this.state.user.fullName}
          </CDropdownItem>
          <CDropdownItem>
            <CIcon name="cil-envelope-open" className="mfe-2" />
          Email:
          {!this.state.user ? "Gmail" : " " + this.state.user.email}
          </CDropdownItem>
          <CDropdownItem divider />
          <CDropdownItem onClick={this.handleLogout}>
            <CIcon name="cil-lock-locked" className="mfe-2" />
          Logout
        </CDropdownItem>
        </CDropdownMenu>
      </CDropdown>
    )
  }
}

export default TheHeaderDropdown
