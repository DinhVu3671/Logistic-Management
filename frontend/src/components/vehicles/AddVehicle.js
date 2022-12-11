import {
    CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CToast,
    CToastBody,
    CToastHeader,
    CToaster,
    CSelect,
    CSwitch,
} from '@coreui/react';
import React, { Component } from 'react'
import vehicleService from '../../service/VehicleService';
import goodsGroupsService from '../../service/GoodsGroupService';
import { hhmmToSeconds, listVehicleType } from '../../utilities/utility'
import MultiSelect from "@khanacademy/react-multi-select";

class AddVehicle extends Component {
    constructor(props) {
        super(props)
        this.state = {
            name: '',
            maxLoadWeight: '',
            maxCapacity: '',
            averageGasConsume: '',
            gasPrice: '',
            averageFeeTransport: '',
            minVelocity: '',
            maxVelocity: '',
            averageVelocity: '',
            available: true,
            vehicleTypes: listVehicleType,
            type: listVehicleType[0].code,
            values: [],
            excludedGoodsGroups: [],
        };


    }

    componentDidMount() {
        let search = { paged: false };
        goodsGroupsService.search(search).then(response => {
            let goodsGroups = response.data.data;
            goodsGroups = goodsGroups.map((goodsGroup, index) => {
                return ({
                    label: goodsGroup.name,
                    value: goodsGroup,
                });
            })
            this.setState({
                goodsGroups: goodsGroups,
            });
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

    handleChangeExcludeGoodsGroups = (excludedGoodsGroups) => {
        this.setState({ excludedGoodsGroups: excludedGoodsGroups });
    }

    handleChangeName = (e) => {
        this.setState({ name: e.target.value });
    }
    handleChangeMaxLoadWeight = (e) => {
        this.setState({ maxLoadWeight: parseFloat(e.target.value) });
    }
    handleChangeMaxCapacity = (e) => {
        this.setState({ maxCapacity: parseFloat(e.target.value) });
    }
    handleChangeAverageGasConsume = (e) => {
        this.setState({ averageGasConsume: parseFloat(e.target.value) });
    }
    handleChangeGasPrice = (e) => {
        this.setState({ gasPrice: parseInt(e.target.value) });
    }
    handleChangeMinVelocity = (e) => {
        this.setState({ minVelocity: parseInt(e.target.value) });
    }
    handleChangeMaxVelocity = (e) => {
        this.setState({ maxVelocity: parseInt(e.target.value) });
    }
    handleChangeAvailable = () => {
        this.setState({ available: !this.state.available });
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    setShowToast = (showToast) => {
        this.setState({
            showToast: showToast
        });
    }

    handleSubmit = () => {
        let vehicleData = {
            name: this.state.name,
            maxLoadWeight: this.state.maxLoadWeight,
            averageGasConsume: this.state.averageGasConsume,
            length: parseFloat(this.state.length),
            width: parseFloat(this.state.width),
            height: parseFloat(this.state.height),
            driverName: this.state.driverName,
            maxCapacity: Math.round(parseFloat(this.state.length * this.state.width * this.state.height) * 100) / 100,
            gasPrice: this.state.gasPrice,
            averageFeeTransport: Math.round(parseFloat((this.state.averageGasConsume * this.state.gasPrice) * 100)) / 100,
            minVelocity: this.state.minVelocity,
            maxVelocity: this.state.maxVelocity,
            averageVelocity: ((this.state.minVelocity + this.state.maxVelocity) / 2),
            available: this.state.available,
            type: this.state.type,
            fixedCost: this.state.fixedCost,
            excludedGoodsGroups: this.state.excludedGoodsGroups,
        }
        console.log(vehicleData);
        vehicleService.create(vehicleData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.showSuccessMsg("Add vehicle " + data.data.name + " successfully");
                this.props.setShowAdd(false);
                this.props.reloadData();
            }

        })
    }

    render() {
        return (
            <CCol>
                <CModal
                    show={true}
                    onClose={() => this.props.setShowAdd(false)}
                    color="success"
                >
                    <CModalHeader closeButton>
                        <CModalTitle>Create Vehicle</CModalTitle>
                    </CModalHeader>
                    <CModalBody>
                        <CCard>
                            <CCardHeader>
                                Vehicle Form
                            </CCardHeader>
                            <CCardBody>
                                <CFormGroup>
                                    <CLabel htmlFor="vehicleName">Name</CLabel>
                                    <CInput id="vehicleName" value={this.state.name} onChange={this.handleChangeName} placeholder="Enter your vehicle name" />
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="10">
                                        <CFormGroup>
                                            <CLabel htmlFor="driverName">Driver's Name</CLabel>
                                            <CInput id="driverName" name="driverName" value={this.state.driverName} onChange={this.handleInputChange} placeholder="driverName" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="2">
                                        <CFormGroup>
                                            <CLabel htmlFor="available">Available</CLabel>
                                            <CSwitch id='available' size="lg" checked={this.state.available} className={'mx-1'} variant={'3d'} color={'success'} onChange={this.handleChangeAvailable} />
                                        </CFormGroup>
                                    </CCol>

                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="maxLoadWeight">Max Load Weight(kg)</CLabel>
                                            <CInput id="maxLoadWeight" value={this.state.maxLoadWeight} onChange={this.handleChangeMaxLoadWeight} placeholder="maxLoadWeight" type='number' step="0.1" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="height">Height (m)</CLabel>
                                            <CInput id="height" name="height" value={this.state.height} onChange={this.handleInputChange} placeholder="height" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="length">Length (m)</CLabel>
                                            <CInput id="length" name="length" value={this.state.length} onChange={this.handleInputChange} placeholder="length" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="width">Width (m)</CLabel>
                                            <CInput id="width" name="width" value={this.state.width} onChange={this.handleInputChange} placeholder="width" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="averageGasConsume">Average Gas Consume(l/km)</CLabel>
                                            <CInput id="averageGasConsume" value={this.state.averageGasConsume} onChange={this.handleChangeAverageGasConsume} placeholder="Enter vehicle average Gas Consume" type='number' step="0.01" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="gasPrice">Gas Price(VND/l)</CLabel>
                                            <CInput id="gasPrice" value={this.state.gasPrice} onChange={this.handleChangeGasPrice} placeholder="Enter vehicle gas Price" type='number' />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="minVelocity">Min Velocity(km/h)</CLabel>
                                            <CInput id="minVelocity" value={this.state.minVelocity} onChange={this.handleChangeMinVelocity} placeholder="Enter vehicle min Velocity" type='number' />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="maxVelocity">Max Velocity(km/h)</CLabel>
                                            <CInput id="maxVelocity" value={this.state.maxVelocity} onChange={this.handleChangeMaxVelocity} placeholder="Enter vehicle max Velocity" type='number' />
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="fixedCost">Fixed Cost</CLabel>
                                            <CInput id="fixedCost" name="fixedCost" value={this.state.fixedCost} onChange={this.handleInputChange} placeholder="Enter vehicle's fixed cost" type='number' step="0.1" />
                                        </CFormGroup>
                                    </CCol>
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="vehicleType">Vehicle Type</CLabel>
                                            <CSelect custom size="md" name="type" id="vehicleType" value={this.state.type} onChange={this.handleInputChange}>
                                                {this.state.vehicleTypes.map((type, index) => <option key={index} value={type.code}>{type.description}</option>)}
                                            </CSelect>
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                                <CFormGroup row className="my-0">
                                    <CCol xs="6">
                                        <CFormGroup>
                                            <CLabel htmlFor="excludedGoodsGroups">Exclude GoodsGroups</CLabel>
                                            {this.state.goodsGroups &&
                                                <MultiSelect
                                                    options={this.state.goodsGroups}
                                                    selected={this.state.excludedGoodsGroups}
                                                    onSelectedChanged={selected => this.handleChangeExcludeGoodsGroups(selected)}
                                                />
                                            }
                                        </CFormGroup>
                                    </CCol>
                                </CFormGroup>
                            </CCardBody>
                        </CCard>
                    </CModalBody>
                    <CModalFooter>
                        <CButton color="success" onClick={() => this.handleSubmit()}>Submit</CButton>{' '}
                        <CButton color="secondary" onClick={() => this.props.setShowAdd(false)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default AddVehicle;
