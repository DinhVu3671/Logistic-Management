import { CButton, CCard, CCardBody, CCardFooter, CCardHeader, CContainer } from "@coreui/react";
import React, { Component } from "react";
import { withNamespaces } from 'react-i18next';

class InitializationType extends Component {

    constructor(props) {
        super(props)
        this.state = {
        };

    }

    handleQuickStart = () => {
        this.props.goToStep(3);
    }

    handleCustomization = () => {
        this.props.goToStep(1);
    }

    render() {
        const { t } = this.props
        return (
            <CContainer>
                <CCard>
                    <CCardHeader>
                        {t('initializationType.title')}
                    </CCardHeader>
                    <CCardBody>
                        <p>
                            <b>{t('initializationType.quickCreate.title')} </b>
                            <i>{t('initializationType.quickCreate.note')}</i>
                        </p>
                        <p>
                            <b>{t('initializationType.customize.title')} </b>
                            <i>{t('initializationType.customize.note')}</i>
                        </p>
                    </CCardBody>
                    <CCardFooter>
                        <CButton color="success" onClick={() => this.handleQuickStart()}>{t('initializationType.quickCreate.button')}</CButton>{' '}
                        <CButton color="warning" onClick={() => this.handleCustomization()}>{t('initializationType.customize.button')}</CButton>
                    </CCardFooter>
                </CCard>
            </CContainer>
        );
    }

}

export default withNamespaces('solution')(InitializationType);