import { CButton, CCard, CCardBody, CCardHeader, CCol, CFormGroup, CInput, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CSelect, CSwitch, CToast, CToastBody, CToaster, CToastHeader } from '@coreui/react';
import React, { Component } from 'react'
import vehicleService from '../../service/VehicleService';
import { listVehicleType } from '../../utilities/utility'
import MultiSelect from "@khanacademy/react-multi-select";
import goodsGroupsService from '../../service/GoodsGroupService';

class EditVehicle extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.editVehicle.id,
            name: this.props.editVehicle.name,
            maxLoadWeight: this.props.editVehicle.maxLoadWeight,
            maxCapacity: this.props.editVehicle.maxCapacity,
            height: this.props.editVehicle.height,
            width: this.props.editVehicle.width,
            length: this.props.editVehicle.length,
            driverName: this.props.editVehicle.driverName,
            averageGasConsume: this.props.editVehicle.averageGasConsume,
            gasPrice: this.props.editVehicle.gasPrice,
            averageFeeTransport: this.props.editVehicle.averageFeeTransport,
            minVelocity: this.props.editVehicle.minVelocity,
            maxVelocity: this.props.editVehicle.maxVelocity,
            averageVelocity: this.props.editVehicle.averageVelocity,
            available: this.props.editVehicle.available,
            type: this.props.editVehicle.type,
            fixedCost: this.props.editVehicle.fixedCost,
            excludedGoodsGroups: this.props.editVehicle.excludedGoodsGroups,
        };
    }

    componentDidMount() {
        this.setState({
            id: this.props.editVehicle.id,
            name: this.props.editVehicle.name,
            maxLoadWeight: this.props.editVehicle.maxLoadWeight,
            maxCapacity: this.props.editVehicle.maxCapacity,
            averageGasConsume: this.props.editVehicle.averageGasConsume,
            gasPrice: this.props.editVehicle.gasPrice,
            averageFeeTransport: this.props.editVehicle.averageFeeTransport,
            minVelocity: this.props.editVehicle.minVelocity,
            maxVelocity: this.props.editVehicle.maxVelocity,
            averageVelocity: this.props.editVehicle.averageVelocity,
            available: this.props.editVehicle.available,
            type: this.props.editVehicle.type,
            excludedGoodsGroups: this.props.editVehicle.excludedGoodsGroups,
            vehicleTypes: listVehicleType,
        });
        let search = { paged: false };
        let excludedGoodsGroupIds = this.props.editVehicle.excludedGoodsGroups.map(goodsGroup => goodsGroup.id);
        goodsGroupsService.search(search).then(response => {
            let goodsGroupsData = response.data.data;
            let excludedGoodsGroups = [];
            let goodsGroups = [];
            goodsGroupsData.map(goodsGroup => {
                if (excludedGoodsGroupIds.includes(goodsGroup.id))
                    excludedGoodsGroups.push(goodsGroup);
                goodsGroups.push({
                    label: goodsGroup.name,
                    value: goodsGroup
                });
            })
            this.setState({
                goodsGroups: goodsGroups,
                excludedGoodsGroups: excludedGoodsGroups,
            });
        });
    }

    handleChangeExcludeGoodsGroups = (excludedGoodsGroupIds) => {
        this.setState({ excludedGoodsGroups: excludedGoodsGroupIds });
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        this.setState({
            [name]: value
        });
    }

    handleChangeName = (e) => {
        this.setState({ name: e.target.value });
    }
    handleChangeMaxLoadWeight = (e) => {
        this.setState({ maxLoadWeight: parseFloat(e.target.value) });
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

    handleSubmit = () => {
        console.log(this.state)
        let vehicleData = {
            id: this.state.id,
            name: this.state.name,
            maxLoadWeight: this.state.maxLoadWeight,
            length: parseFloat(this.state.length),
            width: parseFloat(this.state.width),
            height: parseFloat(this.state.height),
            driverName: this.state.driverName,
            maxCapacity: Math.round(parseFloat(this.state.length * this.state.width * this.state.height) * 100) / 100,
            averageGasConsume: this.state.averageGasConsume,
            gasPrice: this.state.gasPrice,
            averageFeeTransport: (this.state.averageGasConsume * this.state.gasPrice),
            minVelocity: this.state.minVelocity,
            maxVelocity: this.state.maxVelocity,
            averageVelocity: ((this.state.minVelocity + this.state.maxVelocity) / 2),
            available: this.state.available,
            type: this.state.type,
            fixedCost: this.state.fixedCost,
            excludedGoodsGroups: this.state.excludedGoodsGroups,
        }
        vehicleService.update(vehicleData.id, vehicleData).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.props.showSuccessMsg("Edit vehicle " + vehicleData.name + " successfully!");
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
                        <CModalTitle>Edit Vehicle</CModalTitle>
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
                                            {this.state.vehicleTypes &&
                                                <CSelect custom size="md" name="type" id="vehicleType" value={this.state.type} onChange={this.handleInputChange}>
                                                    {this.state.vehicleTypes.map((type, index) => <option key={index} value={type.code}>{type.description}</option>)}
                                                </CSelect>
                                            }
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
                        <CButton color="secondary" onClick={() => this.props.setShowEdit(false)}>Cancel</CButton>
                    </CModalFooter>
                </CModal>
            </CCol>
        );
    }

}

export default EditVehicle;
