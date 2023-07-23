import {createStandaloneToast} from '@chakra-ui/react'

const {toast} = createStandaloneToast()

const notifiation = (title, description, status) => {
    toast({
        title,
        description,
        status,
        isClosable:true,
        duration: 4000
    })
}

export const successNotification = (title, description) => {
    notifiation(
        title,
        description,
        "success"
    )
}

export const errorNotification = (title, description) => {
    notifiation(
        title,
        description,
        "error"
    )
}