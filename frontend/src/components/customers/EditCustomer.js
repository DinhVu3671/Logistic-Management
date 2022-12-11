import { CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast, CToastBody, CToaster, CToastHeader } from '@coreui/react';
import React, { Component } from 'react'
import customerService from '../../service/CustomerService';
import { hhmmToSeconds, secondsToHHMM } from '../../utilities/utility'

class EditCustomer extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.editCustomer.id,
            code: this.props.editCustomer.code,
            name: this.props.editCustomer.name,
            address: this.props.editCustomer.address,
            latitude: this.props.editCustomer.latitude,
            longitude: this.props.editCustomer.longitude,
            startTime: secondsToHHMM(this.props.editCustomer.startTime),
            endTime: secondsToHHMM(this.props.editCustomer.endTime),
            penaltyCost: this.props.editCustomer.penaltyCost,
        };
    }

    componentDidMount() {
        this.setState({
            id: this.props.editCustomer.id,
            code: this.props.editCustomer.code,
            name: this.props.editCustomer.name,
            address: this.props.editCustomer.address,
            latitude: this.props.editCustomer.latitude,
            longitude: this.props.editCustomer.longitude,
            startTime: secondsToHHMM(this.props.editCustomer.startTime),
            endTime: secondsToHHMM(this.props.editCustomer.endTime),
            penaltyCost: this.props.editCustomer.penaltyCost,
        });
    }

    handleChangeName = (e) => {
        this.setState({ name: e.target.value });
    }

    handleChangeAddress = (e) => {
        this.setState({ address: e.target.value });
    }
    handleChangeLatitude = (e) => {
        this.setState({ latitude: e.target.value });
    }
    handleChangeLongitude = (e) => {
        this.setState({ longitude: e.target.value });
    }
    handleChangeStartTime = (e) => {
        this.setState({ startTime: e.target.value });
    }
    handleChangeEndTime = (e) => {
        this.setState({ endTime: e.target.value });
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
        console.log(this.state)
        let customerData = {
            id: this.state.id,
            code: this.state.code,
            name: this.state.name,
            address: this.state.address,
            latitude: parseFloat(this.state.latitude),
            longitude: parseFloat(this.state.longitude),
            startTime: hhmmToSeconds(this.state.startTime),
            endTime: hhmmToSeconds(this.state.endTime),
            penaltyCost: this.state.penaltyCost,
        }
        console.log("customerData: ",customerData);
        customerService.update(customerData.id, customerData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.showSuccessMsg("Edit customer "+customerData.code+" successfully!");
                this.props.setShowEdit(false);
                this.props.reloadData();
            }

        })
    }


    render() {
        return (
            <CCol>
                
                <CModal
                    show={true}
                    onClose={() => this.props.setShowEdit(false)}
                    color="warning"
                >
                    <CModalHeader closeButton>
                        <CModalTitle>Edit Customer</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                Customer Form
                                        </CCardHeader>
                            <CCardBody>
                                <CFormGroup>
                                    <CLabel htmlFor="customerName">Name</CLabel>
                                    <CInput id="customerName" value={this.state.name} onChange={this.handleChangeName} placeholder="Enter your customer name" ></CInput>
                                </CFormGroup>
                                <CFormGroup>
                                    <CLabel htmlFor="address">Address</CLabel>
                                    <CInput id="address" value={this.state.address} onChange={this.handleChangeAddress} placeholder="Enter your customer address" type='String' />
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="latitude">Latitude</CLabel>
                                            <CInput id="latitude" value={this.state.latitude} onChange={this.handleChangeLatitude} placeholder="latitude" type='String' />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="longitude">Longitude</CLabel>
                                            <CInput id="longitude" value={this.state.longitude} onChange={this.handleChangeLongitude} placeholder="longitude" type='String' />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="startTime">Start Time</CLabel>
                                            <CInput type="time" id="startTime" value={this.state.startTime} onChange={this.handleChangeStartTime} placeholder="Enter customer startTime" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="endTime">End Time</CLabel>
                                            <CInput type="time" id="endTime" value={this.state.endTime} onChange={this.handleChangeEndTime} placeholder="Enter customer endTime" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup>
                                    <CLabel htmlFor="penaltyCost">penaltyCost</CLabel>
                                    <CInput id="penaltyCost" name="penaltyCost" value={this.state.penaltyCost} onChange={this.handleInputChange} placeholder="Enter your depot penaltyCost" type='number' />
                                </CFormGroup>
                            </CCardBody>
                        </CCard>
                    </CModalBody>
                    <CModalFooter>
                        <CButton color="success" onClick={() => this.handleSubmit()}>Submit</CButton>{' '}
                        <CButton color="secondary" onClick={() => this.props.setShowEdit(false)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default EditCustomer;
