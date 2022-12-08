import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import {
  CButton,
  CCard,
  CCardBody,
  CCardGroup,
  CCol,
  CContainer,
  CForm,
  CInput,
  CInputGroup,
  CInputGroupPrepend,
  CInputGroupText,
  CRow,
  CSpinner,
  CToast,
  CToastBody,
  CToaster,
  CToastHeader
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import userService from '../../../service/UserService'
import GoogleLogin from 'react-google-login'
import { useHistory, useLocation } from 'react-router-dom'

class Login extends Component {

  state = {
    showToast: false,
    isWaitingRegister: false,
    isWaitingLogin: false,
  }

  handleInputChange = (event) => {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
    this.setState({
      [name]: value
    });
  }

  handleSubmit = () => {
    this.setState({
      isWaitingLogin: true
    });
    const user = {
      email: this.state.email,
      password: this.state.password
    }

    userService.login(user).then(response => {
      if (response.data.code === "LOGIN_SUCCESS") {
        this.props.setUserSession(response.data.data);
      } else {
        this.setState({
          showToast: true,
          msg: response.data.message,
          isWaitingLogin: false,
        });
      }

    });
  }


  responseGoogle = (response) => {
    console.log(response);
  }


  loginGoogleSuccess = (response) => {
    this.setState({ isWaitingRegister: true });
    console.log(response);
    const gmail = response.profileObj.email;
    const user = {
      email: gmail,
      userName: gmail.substring(0, gmail.indexOf("@")),
      fullName: response.profileObj.name,
      avatar: response.profileObj.imageUrl
    }

    userService.signinByGmail(user).then(response => {
      this.setState({ isWaitingRegister: false });
      if (response.data.code === "SUCCESS") {
        this.setState({
          showToast: true,
          msg: "Tạo tài khoản thành công, mật khẩu đã được gửi về hòm thư",
        });
      } else {
        this.setState({
          showToast: true,
          msg: response.data.message,
        });
      }
    }).catch(error => {
      if (error.response) {
        this.setState({
          showToast: true,
          msg: error.response.data.message,
          isWaitingRegister: false,
        });
      }
    });
  }

  setShowToast = (showToast) => {
    this.setState({
      showToast: showToast
    });
  }


  render() {

    return (
      <div className="c-app c-default-layout flex-row align-items-center">
        <CContainer>

          <CToaster position='top-right'>
            <CToast
              key={'toastSuccess'}
              show={this.state.showToast}
              autohide={5000}
              fade={true}
              onStateChange={(showToast) => { this.setShowToast(showToast) }}
            >
              <CToastHeader closeButton>Notification</CToastHeader>
              <CToastBody>
                {this.state.msg}
              </CToastBody>
            </CToast>
          </CToaster>

          <CRow className="justify-content-center">
            <CCol md="5">
              <CCardGroup>
                <CCard className="p-4">
                  <CCardBody>
                    <CForm>
                      <h1>Login</h1>
                      <p className="text-muted">Sign In to your account</p>
                      <CInputGroup className="mb-3">
                        <CInputGroupPrepend>
                          <CInputGroupText>
                            <CIcon name="cil-user" />
                          </CInputGroupText>
                        </CInputGroupPrepend>
                        <CInput name="email" id="email" type="text" placeholder="Email" autoComplete="email" onChange={this.handleInputChange} />
                      </CInputGroup>
                      <CInputGroup className="mb-4">
                        <CInputGroupPrepend>
                          <CInputGroupText>
                            <CIcon name="cil-lock-locked" />
                          </CInputGroupText>
                        </CInputGroupPrepend>
                        <CInput name="password" id="password" type="password" placeholder="Password" autoComplete="current-password" onChange={this.handleInputChange} />
                      </CInputGroup>
                      <CRow>
                        <CCol xs="6">
                          {this.state.isWaitingLogin === true &&
                            <CSpinner size='sm' color='green'></CSpinner>
                          }
                          <CButton color="primary" className="px-4" onClick={this.handleSubmit}>Login</CButton>
                        </CCol>
                        <CCol xs="6" className="text-right">
                          {this.state.isWaitingRegister === true &&
                            <CSpinner size='sm' color='green'></CSpinner>
                          }
                          <GoogleLogin
                            clientId="741705242797-2d9nsjglikbcs515qb6qrvu68udsvnp8.apps.googleusercontent.com"
                            buttonText="Register"
                            onSuccess={this.loginGoogleSuccess}
                            onFailure={this.responseGoogle}
                            cookiePolicy={'single_host_origin'}
                          />,
                        </CCol>
                      </CRow>
                    </CForm>
                  </CCardBody>
                </CCard>
              </CCardGroup>
            </CCol>
          </CRow>
        </CContainer>
      </div>
    )



  }
}

export default Login
