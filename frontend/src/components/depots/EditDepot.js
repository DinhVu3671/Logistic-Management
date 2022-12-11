import { CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast, CToastBody, CToaster, CToastHeader } from '@coreui/react';
import React, { Component } from 'react'
import depotService from '../../service/DepotService';
import productService from '../../service/ProductService';
import { hhmmToSeconds, secondsToHHMM } from '../../utilities/utility'
import MultiSelect from "@khanacademy/react-multi-select";

class EditDepot extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.editDepot.id,
            code: this.props.editDepot.code,
            name: this.props.editDepot.name,
            address: this.props.editDepot.address,
            latitude: this.props.editDepot.latitude,
            longitude: this.props.editDepot.longitude,
            startTime: secondsToHHMM(this.props.editDepot.startTime),
            endTime: secondsToHHMM(this.props.editDepot.endTime),
            unloadingCost: this.props.editDepot.unloadingCost,
        };
    }

    componentDidMount() {
        this.setState({
            id: this.props.editDepot.id,
            code: this.props.editDepot.code,
            name: this.props.editDepot.name,
            address: this.props.editDepot.address,
            latitude: this.props.editDepot.latitude,
            longitude: this.props.editDepot.longitude,
            startTime: secondsToHHMM(this.props.editDepot.startTime),
            endTime: secondsToHHMM(this.props.editDepot.endTime),
            unloadingCost: this.props.editDepot.unloadingCost,
        });
        let availableProductIds = this.props.editDepot.products.map(product => product.id);
        let search = { paged: false };
        productService.search(search).then(response => {
            let products = response.data.data;
            this.setState({
                products: products,
                availableProductIds: availableProductIds,
            });
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
    handleChangeUnloadingCost = (e) => {
        this.setState({ unloadingCost: e.target.value });
    }

    handleChangeProducts = (productIds) => {
        this.setState({ availableProductIds: productIds });
    }

    handleSubmit = () => {
        console.log(this.state)
        let depotData = {
            id: this.state.id,
            code: this.state.code,
            name: this.state.name,
            address: this.state.address,
            latitude: parseFloat(this.state.latitude),
            longitude: parseFloat(this.state.longitude),
            startTime: hhmmToSeconds(this.state.startTime),
            endTime: hhmmToSeconds(this.state.endTime),
            unloadingCost: this.state.unloadingCost,
            products: this.state.products.filter(product => this.state.availableProductIds.includes(product.id))
        }
        depotService.update(depotData.id, depotData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.showSuccessMsg("Edit depot " + depotData.code + " successfully!");
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
                        <CModalTitle>Edit Depot</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                Depot Form
                            </CCardHeader>
                            <CCardBody>
                                <CFormGroup>
                                    <CLabel htmlFor="depotName">Name</CLabel>
                                    <CInput id="depotName" value={this.state.name} onChange={this.handleChangeName} placeholder="Enter your depot name" ></CInput>
                                </CFormGroup>
                                <CFormGroup>
                                    <CLabel htmlFor="address">Address</CLabel>
                                    <CInput id="address" value={this.state.address} onChange={this.handleChangeAddress} placeholder="Enter your depot address" type='String' />
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
                                            <CInput type="time" id="startTime" value={this.state.startTime} onChange={this.handleChangeStartTime} placeholder="Enter depot startTime" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="endTime">End Time</CLabel>
                                            <CInput type="time" id="endTime" value={this.state.endTime} onChange={this.handleChangeEndTime} placeholder="Enter depot endTime" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup>
                                    <CLabel htmlFor="unloadingCost">UnloadingCost</CLabel>
                                    <CInput id="unloadingCost" value={this.state.unloadingCost} onChange={this.handleChangeUnloadingCost} placeholder="Enter your depot unloadingCost" type='number' />
                                </CFormGroup>
                                <CFormGroup row >
                                    <CCol xs="12">
                                        <CLabel htmlFor="products">Products</CLabel>
                                        {this.state.products &&
                                            <MultiSelect
                                                options={this.state.products.map(product => {
                                                    return ({
                                                        label: product.name,
                                                        value: product.id,
                                                    });
                                                })}
                                                selected={this.state.availableProductIds}
                                                onSelectedChanged={selected => this.handleChangeProducts(selected)}
                                            />
                                        }
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

export default EditDepot;
