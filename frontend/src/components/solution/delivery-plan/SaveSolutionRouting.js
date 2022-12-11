import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CSelect,
    CInputGroup,
    CInputGroupAppend,
    CSpinner,
} from '@coreui/react';
import React, { Component } from 'react'
import routeService from '../../../service/SolutionRouteService';

class SaveSolutionRouting extends Component {
    constructor(props) {
        super(props)
        this.state = {
            solution: this.props.solution,
            problemAssumption: this.props.problemAssumption,
            isWaiting: false,
        };

    }

    componentDidMount() {

        this.setState({
            solution: this.state.solution,
            problemAssumption: this.props.problemAssumption,
        });

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
            isWaiting: true,
        });

        let solutionRouting = {
            solution: this.state.solution,
            problemAssumption: this.state.problemAssumption,
            name: this.state.name,
            intendReceiveTime: new Date(this.state.intendReceiveTime).getTime()
        }


        routeService.saveSolution(solutionRouting).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.setState({
                    isWaiting: false,
                });
                this.props.setShowSaveSolutionForm(false);
                this.props.showSuccessMsg("Save solution " + data.data.name + " successfully");
            }

        })
    }


    render() {
        return (
            <CCol>
                <CModal
                    show={true}
                    onClose={() => this.props.setShowSaveSolutionForm(false)}
                    color="success"
                >
                    <CModalHeader closeButton>
                        <CModalTitle>Save Solution Routing</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                Routing Solution Form
                                        </CCardHeader>
                            <CCardBody>
                                <CFormGroup row>
                                    <CCol md="3">
                                        <CLabel htmlFor="name">Name</CLabel>
                                    </CCol>
                                    <CCol xs="12" md="9">
                                        <CInput type="text" id="name" name="name" placeholder="name" onChange={this.handleInputChange} />
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row>
                                    <CCol md="3">
                                        <CLabel htmlFor="intendReceiveTime">Intend Date</CLabel>
                                    </CCol>
                                    <CCol xs="12" md="9">
                                        <CInput type="date" id="intendReceiveTime" name="intendReceiveTime" placeholder="date" onChange={this.handleInputChange} />
                                    </CCol>
                                </CFormGroup>
                            </CCardBody>
                        </CCard>
                    </CModalBody>
                    <CModalFooter>
                        {this.state.isWaiting === true &&
                            <CSpinner size='sm' color='green'></CSpinner>
                        }
                        <CButton color="success" onClick={() => this.handleSubmit()}>Submit</CButton>{' '}
                        <CButton color="secondary" onClick={() => this.props.setShowSaveSolutionForm(false)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default SaveSolutionRouting;
