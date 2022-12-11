import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CToastBody,
    CToastHeader,
    CToaster,
} from '@coreui/react';
import React, { Component } from 'react'
import depotService from '../../service/DepotService';
import { hhmmToSeconds } from '../../utilities/utility'
import productService from '../../service/ProductService';
import MultiSelect from "@khanacademy/react-multi-select";

class AddDepot extends Component {
    constructor(props) {
        super(props)
        this.state = {
            show: this.props.show,
            name: '',
            address: '',
            latitude: '',
            longitude: '',
            startTime: '',
            endTime: '',
            unloadingCost: '',
            showToast: false,
        };

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

    setShowToast = (showToast) => {
        this.setState({
            showToast: showToast
        });
    }

    componentDidMount() {
        let search = { paged: false };
        productService.search(search).then(response => {
            let products = response.data.data;
            this.setState({
                products: products,
                availableProductIds: products.map(product => product.id),
            });
        });
    }

    handleSubmit = () => {
        console.log(this.state)
        let depotData = {
            name: this.state.name,
            address: this.state.address,
            latitude: parseFloat(this.state.latitude),
            longitude: parseFloat(this.state.longitude),
            startTime: hhmmToSeconds(this.state.startTime),
            endTime: hhmmToSeconds(this.state.endTime),
            unloadingCost: this.state.unloadingCost,
            products: this.state.products.filter(product => this.state.availableProductIds.includes(product.id))
        }
        depotService.create(depotData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.setShowAdd(false);
                this.setState({
                    msg: ("Add depot " + data.data.name + " successfully"),
                    showToast: true
                });
                this.props.reloadData();
            }

        })
    }

    handleChangeProducts = (productIds) => {
        this.setState({ availableProductIds: productIds });
    }

    render() {
        const show = this.props.show;
        return (
            <CCol>
                <CCol sm="12" lg="6">
                    <CToaster position='top-right'>
                        <CToast
                            key={'toastSuccess'}
                            show={this.state.showToast}
                            autohide={4000}
                            fade={true}
                            onStateChange={(showToast) => { this.setShowToast(showToast) }}
                        >
                            <CToastHeader closeButton>Notification</CToastHeader>
                            <CToastBody>
                                {this.state.msg}
                            </CToastBody>
                        </CToast>
                    </CToaster>
                </CCol>
                <CModal
                    show={show}
                    onClose={() => this.props.setShowAdd(!this.props.show)}
                    color="success"
                >
                    <CModalHeader closeButton>
                        <CModalTitle>Create Depot</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                Depot Form
                                        </CCardHeader>
                            <CCardBody>
                                <CFormGroup>
                                    <CLabel htmlFor="depotName">Name</CLabel>
                                    <CInput id="depotName" value={this.state.name} onChange={this.handleChangeName} placeholder="Enter your depot name" />
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
                        <CButton color="secondary" onClick={() => this.props.setShowAdd(!this.props.show)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default AddDepot;
