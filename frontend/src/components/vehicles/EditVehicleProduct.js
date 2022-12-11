import { CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CSelect, CSwitch, CToast, CToastBody, CToaster, CToastHeader } from '@coreui/react';
import React, { Component } from 'react'
import vehicleService from '../../service/VehicleService';

class EditVehicleProduct extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.editVehicleProduct.id,
            maxNumber: this.props.editVehicleProduct.maxNumber,
            product: this.props.editVehicleProduct.product,
            vehicle: this.props.editVehicleProduct.vehicle,
        };
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
        let vehicleProduct = {
            id: this.state.id,
            maxNumber: this.state.maxNumber,
            product: this.state.product,
            vehicle: this.state.vehicle,
        }
        vehicleService.updateVehicleProduct(vehicleProduct.id, vehicleProduct.vehicle.id, vehicleProduct).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.showSuccessMsg("Edit vehicle successfully!");
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
                        <CModalTitle>Edit Max Product Number Of Vehicle </CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardBody>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="maxNumber">Max Number Product</CLabel>
                                            <CInput id="maxNumber" name="maxNumber" value={this.state.maxNumber} onChange={this.handleInputChange} placeholder="maxNumber" type='number' />
                                        </CFormGroup>
                                    </CCol>
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

export default EditVehicleProduct;
